package com.microservices.store.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.domain.PhonePrice;
import com.microservices.store.service.ExchangeRateMicroserviceCommand;
import com.microservices.store.service.PhoneStoreRepo;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@RestController
public class CalculatePriceController
{
	private static final Logger LOGGER = Logger.getLogger(CalculatePriceController.class);
	
	@Autowired
	private PhoneStoreRepo phoneStoreRepo;
	
	@RequestMapping(value = "getPhonePrice", method = RequestMethod.GET)
	public PhonePrice getPhonePrice(String phoneModel) throws InterruptedException
	{
		RestTemplate restTemplate = new RestTemplate();
		double exchangeRate;
		try
		{
			ExchangeRate exchangeRateResponseEntity = restTemplate.getForObject("http://localhost:7001/getCurrentUAHExchangeRate?currency={currency}", ExchangeRate.class, "USD");
			exchangeRate = exchangeRateResponseEntity.getExchangeRate();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			exchangeRate = ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get("USD");
		}
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRate);
	}
	
	@RequestMapping(value = "getPhonePriceWithHystrix", method = RequestMethod.GET)
	public PhonePrice getPhonePriceWithHystrix(String phoneModel) throws InterruptedException, ExecutionException
	{
		HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
		
		try
		{
			ExchangeRateMicroserviceCommand exchangeRateMicroserviceCommand = new ExchangeRateMicroserviceCommand("USD");
			Future<ExchangeRate> exchangeRate = exchangeRateMicroserviceCommand.queue();
			double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
			ExchangeRate exchangeRateResponse = exchangeRate.get();
			
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
			
			LOGGER.info("USD to EUR rate: " + ExchangeRateUtil.calculateUsdToEurExcahngeRate());
			
			return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRateResponse.getExchangeRate());
		}
		finally {
			hystrixRequestContext.shutdown();
		}
	}
}
