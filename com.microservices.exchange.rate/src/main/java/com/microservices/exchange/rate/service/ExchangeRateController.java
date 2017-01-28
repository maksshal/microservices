package com.microservices.exchange.rate.service;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.exchange.rate.domain.ExchangeRate;
import com.microservices.exchange.rate.util.ExchangeRateUtil;

@RestController
public class ExchangeRateController
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class);
	
	private static final Random RANDOM = new Random();
	
	@RequestMapping(method = RequestMethod.GET)
	public ExchangeRate getCurrentUAHExchangeRate(String currency) throws InterruptedException
	{
		LOGGER.info("Request arrived to get exchange rate for currency: " + currency);
//		Thread.sleep(10_000);
		
		if(RANDOM.nextBoolean())
		{
			return new ExchangeRate("UAH", currency, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency) + RANDOM.nextDouble());
		}
		else
		{
			return new ExchangeRate("UAH", currency, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency) - RANDOM.nextDouble());
		}
	}
}
