package com.microservices.store.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	public static final String UAH = "UAH";
	
	public static final Map<String, BigDecimal> UAH_EXCHANGE_RATE_DEFAULT;
	static
	{
		Map<String, BigDecimal> defaults = new HashMap<>();
		defaults.put(USD, new BigDecimal("28.2"));
		defaults.put(EUR, new BigDecimal("31.0"));
		UAH_EXCHANGE_RATE_DEFAULT = Collections.unmodifiableMap(defaults);
	}

	/**
	 * Calculate USD/EUR using blocking requests
	 * @return
	 */
	public static BigDecimal calculateUsdToEurExchangeRate()
	{
		ExchangeRate usdToUah = new ExchangeRateMicroserviceCommand(USD).execute();
		ExchangeRate eurToUah = new ExchangeRateMicroserviceCommand(EUR).execute();
		
		return usdToUah.getExchangeRate().divide(eurToUah.getExchangeRate(), 2, RoundingMode.HALF_UP);
	}

	/**
	 * Calculate USD/EUR asynchronously (two parallel requests, then combine results)
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static BigDecimal calculateUsdToEurExchangeRateAsync() throws InterruptedException, ExecutionException
	{
		Future<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).queue();
		Future<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).queue();
		
		return usdToUah.get().getExchangeRate().divide(eurToUah.get().getExchangeRate(), 2, RoundingMode.HALF_UP);
	}

	/**
	 * Collapse two similar requests, so only one HTTP call will be executed
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static BigDecimal calculateUsdToEurExchangeRateCollapsed() throws InterruptedException, ExecutionException
	{
		Future<ExchangeRate> usdToUah = new ExchangeRateRequestCollapser(USD).queue();
		Future<ExchangeRate> eurToUah = new ExchangeRateRequestCollapser(EUR).queue();
		
		return usdToUah.get().getExchangeRate().divide(eurToUah.get().getExchangeRate(), 2, RoundingMode.HALF_UP);
	}

	/**
	 * Get hot observable (will be executed immediately) for USD to EUR exchange rate calculation
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Observable<BigDecimal> calculateUsdToEurExchangeRateHotObservable()
	{
		Observable<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).observe();
		Observable<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).observe();
		
		Observable<BigDecimal> usdToEurExchangeRateObservable = Observable.zip(
				usdToUah,
				eurToUah,
				(rateUsd, rateEur) -> rateUsd.getExchangeRate().divide(rateEur.getExchangeRate(), 2, RoundingMode.HALF_UP));
		
		return usdToEurExchangeRateObservable;
	}

	/**
	 * Get cold observable (will be executed only after subscription) for USD to EUR exchange rate calculation
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Observable<BigDecimal> calculateUsdToEurExchangeRateColdObservable()
	{
		Observable<ExchangeRate> usdToUah = new ExchangeRateMicroserviceCommand(USD).toObservable();
		Observable<ExchangeRate> eurToUah = new ExchangeRateMicroserviceCommand(EUR).toObservable();
		
		Observable<BigDecimal> usdToEurExchangeRateObservable = Observable.zip(
				usdToUah,
				eurToUah,
				(rateUsd, rateEur) -> rateUsd.getExchangeRate().divide(rateEur.getExchangeRate(), 2, RoundingMode.HALF_UP));
		
		return usdToEurExchangeRateObservable;
	}
}
