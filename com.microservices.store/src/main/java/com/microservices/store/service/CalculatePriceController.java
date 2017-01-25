package com.microservices.store.service;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CalculatePriceController
{
	private static final double EXCHANGE_RATE_DEFAULT = 27.2;
	private static final Map<String, Double> PHONE_PRICES = new HashMap<>();
	static
	{
		PHONE_PRICES.put("Huawei GR5", 200.0);
		PHONE_PRICES.put("Huawei Y6 Pro", 150.0);
		PHONE_PRICES.put("Huawei P9 Prestige", 666.66);
		
		PHONE_PRICES.put("Meizu M2", 100.0);
		PHONE_PRICES.put("Meizu U10", 200.0);
		PHONE_PRICES.put("Meizu Pro 6", 300.0);
	}
	
	private static final DecimalFormat FORMATTER = new DecimalFormat("###.##");
	
	@RequestMapping(method = RequestMethod.GET)
	public String getPhonePrice(String phoneModel)
	{
		RestTemplate restTemplate = new RestTemplate();
		
		double exchangeRate;
		try
		{
			exchangeRate = restTemplate.getForObject(new URI("http://localhost:7001/getCurrentUSDollarExchangeRate"), Double.class);
		}
		catch (Exception e)
		{
			exchangeRate = EXCHANGE_RATE_DEFAULT;
		}
		
		return FORMATTER.format(PHONE_PRICES.get(phoneModel) * exchangeRate);
	}
}
