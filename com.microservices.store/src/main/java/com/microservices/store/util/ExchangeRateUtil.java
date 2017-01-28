package com.microservices.store.util;

import java.util.HashMap;
import java.util.Map;

import com.microservices.store.service.ExchangeRateMicroserviceCommand;

public class ExchangeRateUtil
{
	public static final Map<String, Double> UAH_EXCHANGE_RATE_DEFAULT = new HashMap<>();
	static
	{
		UAH_EXCHANGE_RATE_DEFAULT.put("USD", 27.2);
		UAH_EXCHANGE_RATE_DEFAULT.put("EUR", 30.0);
	}
		
	public static Double calculateUsdToEurExcahngeRate()
	{
		return new ExchangeRateMicroserviceCommand("USD").execute().getExchangeRate() / new ExchangeRateMicroserviceCommand("EUR").execute().getExchangeRate();
	}
}
