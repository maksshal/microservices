package com.microservices.exchange.rate.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateUtil
{
	/**
	 * UAH exchange rate defaults
	 */
	public static final Map<String, BigDecimal> UAH_EXCHANGE_RATE_DEFAULT = new HashMap<>();
	static
	{
		UAH_EXCHANGE_RATE_DEFAULT.put("USD", new BigDecimal("28.2"));
		UAH_EXCHANGE_RATE_DEFAULT.put("EUR", new BigDecimal("31.0"));
	}
}