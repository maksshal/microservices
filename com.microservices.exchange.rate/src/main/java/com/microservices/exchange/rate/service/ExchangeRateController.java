package com.microservices.exchange.rate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microservices.model.ExchangeRate;
import com.microservices.model.ExchangeRateDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateController
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class);
	
	private static final Random RANDOM = new Random();
	
	@RequestMapping(method = RequestMethod.GET, value = "getCurrentUAHExchangeRate")
	public ResponseEntity<ExchangeRate> getCurrentUAHExchangeRate(String currency) throws InterruptedException
	{
		LOGGER.info("Request arrived to get exchange rate for currency: " + currency);
//		Thread.sleep(10_000);

		ExchangeRate exchangeRate = generateExchangeRate(currency);
		if(exchangeRate == null)
		{
			return new ResponseEntity("Exchange rate not found.", HttpStatus.BAD_REQUEST);
		}
		else
		{
			return new ResponseEntity(exchangeRate, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCurrentUAHExchangeRateForCurrencies")
	public List<ExchangeRate> getCurrentUAHExchangeRateForCurrencies(String currencies)
	{
		LOGGER.info("Request arrived to get exchange rate for currencies: " + currencies);
		
		String[] currenciesList = currencies.split(",");
		List<ExchangeRate> exchangeRates = new ArrayList<>();
		for (String currency : currenciesList)
		{
			ExchangeRate exchangeRate = generateExchangeRate(currency);
			if(exchangeRate != null)
			{
				exchangeRates.add(exchangeRate);
			}
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
		BigDecimal defaultExchangeRate = ExchangeRateDefaults.UAH_EXCHANGE_RATE_DEFAULT.get(currency);
		if(defaultExchangeRate == null)
		{
			return null;
		}

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
