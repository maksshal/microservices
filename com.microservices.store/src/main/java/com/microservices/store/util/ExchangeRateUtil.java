package com.microservices.store.util;

import com.microservices.store.service.ExchangeRateMicroserviceCommand;

public class ExchangeRateUtil
{
	public static final double EXCHANGE_RATE_DEFAULT = 27.2;
	
	public static Double calculateUsdToEurExcahngeRate()
	{
		return new ExchangeRateMicroserviceCommand("USD").execute() / new ExchangeRateMicroserviceCommand("EUR").execute();
	}
}
