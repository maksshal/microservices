package com.microservices.store.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import rx.Observable;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.service.ExchangeRateMicroserviceCommand;
import com.microservices.store.service.ExchangeRateRequestCollapser;

/**
 * Utility methods for testing different Hystrix calls
 */
public class ExchangeRateUtil
{
	public static final String EXCHANGE_RATE_SERVICE_URL = "http://localhost:7001/getCurrentUAHExchangeRate?currency={currency}";

	public static final String USD = "USD";
	public static final String EUR = "EUR";
	
	public static final Map<String, Double> UAH_EXCHANGE_RATE_DEFAULT;
	static
	{
		Map<String, Double> defaults = new HashMap<>();
		defaults.put(USD, 28.2);
		defaults.put(EUR, 31.0);
		UAH_EXCHANGE_RATE_DEFAULT = Collections.unmodifiableMap(defaults);
	}

	/**
	 * Calculate USD/EUR using blocking requests
	 * @return
	 */
	public static Double calculateUsdToEurExchangeRate()
	{
		ExchangeRate usdToUah = new ExchangeRateMicroserviceCommand(USD).execute();
		ExchangeRate eurToUah = new ExchangeRateMicroserviceCommand(EUR).execute();
		
		return usdToUah.getExchangeRate() / eurToUah.getExchangeRate();
	}

	/**
	 * Calculate USD/EUR asynchronously (two parallel requests, then combine results)
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Double calculateUsdToEurExchangeRateAsync() throws InterruptedException, ExecutionException
	{
		Future<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).queue();
		Future<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).queue();
		
		return usdToUah.get().getExchangeRate() / eurToUah.get().getExchangeRate();
	}

	/**
	 * Collapse two similar requests, so only one HTTP call will be executed
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Double calculateUsdToEurExchangeRateCollapsed() throws InterruptedException, ExecutionException
	{
		Future<ExchangeRate> usdToUah = new ExchangeRateRequestCollapser(USD).queue();
		Future<ExchangeRate> eurToUah = new ExchangeRateRequestCollapser(EUR).queue();
		
		return usdToUah.get().getExchangeRate() / eurToUah.get().getExchangeRate();
	}

	/**
	 * Get hot observable (will be executed immediately) for USD to EUR exchange rate calculation
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Observable<Double> calculateUsdToEurExchangeRateHotObservable()
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

	/**
	 * Get cold observable (will be executed only after subscription) for USD to EUR exchange rate calculation
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Observable<Double> calculateUsdToEurExchangeRateColdObservable()
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
