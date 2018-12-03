package com.microservices.exchange.rate.service;

import java.math.BigDecimal;
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
	public List<ExchangeRate> getCurrentUAHExchangeRateForCurrencies(String currencies)
	{
		LOGGER.info("Request arrived to get exchange rate for currencies: " + currencies);
		
		String[] currenciesList = currencies.split(",");
		List<ExchangeRate> exchangeRates = new ArrayList<>();
		for (String currency : currenciesList)
		{
			exchangeRates.add(generateExchangeRate(currency));
		}
		
		return exchangeRates;
	}

	/**
	 * Generate random exchange rate from currency to UAH
	 * @param currency
	 * @return
	 */
	private ExchangeRate generateExchangeRate(String currency)
	{
		BigDecimal defaultExchangeRate = ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency);
		BigDecimal deviation = new BigDecimal(RANDOM.nextDouble());

		BigDecimal currentExchangeRate = RANDOM.nextBoolean() ?
				defaultExchangeRate.add(deviation) : defaultExchangeRate.subtract(deviation);
		currentExchangeRate = currentExchangeRate.setScale(2, BigDecimal.ROUND_HALF_UP);
		return new ExchangeRate("UAH", currency, currentExchangeRate);
	}
	
	@RequestMapping(value = "/")
	public String ping()
	{
		return "OK";
	}
}
