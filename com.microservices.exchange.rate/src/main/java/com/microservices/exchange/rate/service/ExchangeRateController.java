package com.microservices.exchange.rate.service;

import java.text.DecimalFormat;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateController
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class);
	
	private static final double EXCHANGE_RATE_FORECAST_USD = 27.2;
	private static final double EXCHANGE_RATE_FORECAST_EUR = 30.0;
	private static final DecimalFormat FORMATTER = new DecimalFormat("###.##");
	
	private static final Random RANDOM = new Random();
	
	@RequestMapping(method = RequestMethod.GET)
	public String getCurrentUSDollarExchangeRate(String currency) throws InterruptedException
	{
		LOGGER.info("Request arrived to get exchange rate for currency: " + currency);
//		Thread.sleep(10_000);
		
		if(currency.equals("USD"))
		{
			if(RANDOM.nextBoolean())
			{
				return FORMATTER.format(EXCHANGE_RATE_FORECAST_USD + RANDOM.nextDouble());
			}
			else
			{
				return FORMATTER.format(EXCHANGE_RATE_FORECAST_USD - RANDOM.nextDouble());
			}
		}
		
		if(currency.equals("EUR"))
		{
			if(RANDOM.nextBoolean())
			{
				return FORMATTER.format(EXCHANGE_RATE_FORECAST_EUR + RANDOM.nextDouble());
			}
			else
			{
				return FORMATTER.format(EXCHANGE_RATE_FORECAST_EUR - RANDOM.nextDouble());
			}
		}
		
		return null;
	}
}
