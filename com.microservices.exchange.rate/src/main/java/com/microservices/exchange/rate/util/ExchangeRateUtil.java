package com.microservices.exchange.rate.util;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateUtil
{
	public static final Map<String, Double> UAH_EXCHANGE_RATE_DEFAULT = new HashMap<>();
	static
	{
		UAH_EXCHANGE_RATE_DEFAULT.put("USD", 27.2);
		UAH_EXCHANGE_RATE_DEFAULT.put("EUR", 30.0);
	}
}
