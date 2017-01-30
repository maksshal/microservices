package com.microservices.store.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import rx.Observable;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.service.ExchangeRateMicroserviceCommand;

public class ExchangeRateUtil
{
	public static final String USD = "USD";
	public static final String EUR = "EUR";
	
	public static final Map<String, Double> UAH_EXCHANGE_RATE_DEFAULT = new HashMap<>();
	static
	{
		UAH_EXCHANGE_RATE_DEFAULT.put(USD, 27.2);
		UAH_EXCHANGE_RATE_DEFAULT.put(EUR, 30.0);
	}
		
	public static Double calculateUsdToEurExcahngeRate()
	{
		ExchangeRate usdToUah = new ExchangeRateMicroserviceCommand(USD).execute();
		ExchangeRate eurToUah = new ExchangeRateMicroserviceCommand(EUR).execute();
		
		return usdToUah.getExchangeRate() / eurToUah.getExchangeRate();
	}
	
	public static Double calculateUsdToEurExcahngeRateAsync() throws InterruptedException, ExecutionException
	{
		Future<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).queue();
		Future<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).queue();
		
		return usdToUah.get().getExchangeRate() / eurToUah.get().getExchangeRate();
	}
	
	public static Observable<Double> calculateUsdToEurExcahngeRateHotObservable() throws InterruptedException, ExecutionException
	{
		Observable<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).observe();
		Observable<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).observe();
		
		Observable<Double> usdToEurExchangeRateObservable = Observable.zip(
				usdToUah,
				eurToUah,
				(rateUsd, rateEur) -> rateUsd.getExchangeRate()
						/ rateEur.getExchangeRate());
		
		return usdToEurExchangeRateObservable;
	}
	
	public static Observable<Double> calculateUsdToEurExcahngeRateColdObservable() throws InterruptedException, ExecutionException
	{
		Observable<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).toObservable();
		Observable<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).toObservable();
		
		Observable<Double> usdToEurExchangeRateObservable = Observable.zip(
				usdToUah,
				eurToUah,
				(rateUsd, rateEur) -> rateUsd.getExchangeRate()
						/ rateEur.getExchangeRate());
		
		return usdToEurExchangeRateObservable;
	}
}
