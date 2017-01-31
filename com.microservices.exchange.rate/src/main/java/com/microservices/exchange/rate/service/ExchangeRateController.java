package com.microservices.exchange.rate.service;

import java.util.ArrayList;
import java.util.List;
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
	
	@RequestMapping(method = RequestMethod.GET, value = "getCurrentUAHExchangeRate")
	public ExchangeRate getCurrentUAHExchangeRate(String currency) throws InterruptedException
	{
		LOGGER.info("Request arrived to get exchange rate for currency: " + currency);
//		Thread.sleep(10_000);
		
		return generateExchangeRate(currency);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCurrentUAHExchangeRateForCurrencies")
	public List<ExchangeRate> getCurrentUAHExchangeRateForCurrencies(String currencies) throws InterruptedException
	{
		LOGGER.info("Request arrived to get exchange rate for currencies: " + currencies);
		
		String[] curenciesList = currencies.split(",");
		List<ExchangeRate> exchangeRates = new ArrayList<>();
		for (String currency : curenciesList)
		{
			exchangeRates.add(generateExchangeRate(currency));
		}
		
		return exchangeRates;
	}
	
	private ExchangeRate generateExchangeRate(String currency)
	{
		if(RANDOM.nextBoolean())
		{
			return new ExchangeRate("UAH", currency, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency) + RANDOM.nextDouble());
		}
		else
		{
			return new ExchangeRate("UAH", currency, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency) - RANDOM.nextDouble());
		}
	}
	
	@RequestMapping(value = "/")
	public String ping()
	{
		return "OK";
	}
}
