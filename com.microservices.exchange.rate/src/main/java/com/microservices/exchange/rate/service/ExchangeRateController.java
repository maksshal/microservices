package com.microservices.exchange.rate.service;

import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateController
{
	private static final double EXCHANGE_RATE_FORECAST = 27.2;
	private static final DecimalFormat FORMATTER = new DecimalFormat("###.##");
	
	private static final Random RANDOM = new Random();
	
	@RequestMapping(method = RequestMethod.GET)
	public String getCurrentUSDollarExchangeRate() throws InterruptedException
	{
		Thread.sleep(10_000);
		
		if(RANDOM.nextBoolean())
		{
			return FORMATTER.format(EXCHANGE_RATE_FORECAST + RANDOM.nextDouble());
		}
		else
		{
			return FORMATTER.format(EXCHANGE_RATE_FORECAST - RANDOM.nextDouble());
		}
	}
}
