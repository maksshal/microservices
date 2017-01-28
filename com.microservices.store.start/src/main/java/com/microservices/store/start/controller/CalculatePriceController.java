package com.microservices.store.start.controller;

import java.net.URI;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.store.start.service.PhoneStoreRepo;
import com.microservices.store.start.util.ExchangeRateUtil;

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
		LOGGER.info("Getting phone price without Hystrix");
		
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
}
