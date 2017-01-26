package com.microservices.store.controller;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.store.service.ExchangeRateMicroserviceCommand;
import com.microservices.store.service.PhoneStoreRepo;
import com.microservices.store.util.ExchangeRateUtil;

@RestController
public class CalculatePriceController
{
	private static final Logger LOGGER = Logger.getLogger(CalculatePriceController.class);
	private static final DecimalFormat FORMATTER = new DecimalFormat("###.##");
	
	@Autowired
	private PhoneStoreRepo phoneStoreRepo;
	
	@RequestMapping(value = "getPhonePrice", method = RequestMethod.GET)
	public String getPhonePrice(String phoneModel) throws InterruptedException
	{
		RestTemplate restTemplate = new RestTemplate();
		double exchangeRate;
		try
		{
			exchangeRate = restTemplate.getForObject(new URI("http://localhost:7001/getCurrentUSDollarExchangeRate"), Double.class);
		}
		catch (Exception e)
		{
			exchangeRate = ExchangeRateUtil.EXCHANGE_RATE_DEFAULT;
		}
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		return FORMATTER.format(phonePriceInUSD * exchangeRate);
	}
	
	@RequestMapping(value = "getPhonePriceWithHystrix", method = RequestMethod.GET)
	public String getPhonePriceWithHystrix(String phoneModel) throws InterruptedException, ExecutionException
	{
		ExchangeRateMicroserviceCommand exchangeRateMicroserviceCommand = new ExchangeRateMicroserviceCommand();
		Future<Double> exchangeRate = exchangeRateMicroserviceCommand.queue();
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		
		Double exchangeRateValue = exchangeRate.get();
		
		if(exchangeRateMicroserviceCommand.isResponseFromFallback())
		{
			if(exchangeRateMicroserviceCommand.isResponseShortCircuited())
			{
				LOGGER.info("CIRCUIT OPENED, so calling fallback immidiately.");
			}
			else if(exchangeRateMicroserviceCommand.isResponseTimedOut())
			{
				LOGGER.info("Hystrix: request timeout.");
			}
			else
			{
				LOGGER.info("Hystrix: executing fallback.");
			}
		}
		else
		{
			LOGGER.info("Hystrix: request executed successfully.");
		}
		
		return FORMATTER.format(phonePriceInUSD * exchangeRateValue);
	}
}
