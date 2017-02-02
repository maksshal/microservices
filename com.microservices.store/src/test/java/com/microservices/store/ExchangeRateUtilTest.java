package com.microservices.store;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Subscriber;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.service.ObservableExchangeRateCommand;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class ExchangeRateUtilTest
{
	private static final Logger LOGGER = Logger
			.getLogger(ExchangeRateUtilTest.class);
	
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		testObservable();
	}
	
	public static void testCalculateUsdToEurExcahngeRate()
	{
		long start = System.nanoTime();
		ExchangeRateUtil.calculateUsdToEurExcahngeRate();
		long end = System.nanoTime();
		LOGGER.info("SYNC execution time in millis: " + (end - start)
				/ 1_000_000);
	}

	public static void testCalculateUsdToEurExcahngeRateAsync()
			throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		ExchangeRateUtil.calculateUsdToEurExcahngeRateAsync();
		long end = System.nanoTime();
		LOGGER.info("ASYNC execution time in millis: " + (end - start)
				/ 1_000_000);
	}
	
	public static void testCalculateUsdToEurExcahngeRateHotObservable() throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		
		Observable<Double> rateObservable = ExchangeRateUtil.calculateUsdToEurExcahngeRateHotObservable();
		rateObservable.subscribe((result) -> 
		{
			LOGGER.info("Execution COMPLETED, exchange rate received: " + result);
			LOGGER.info("Time elapsed: " + (System.nanoTime() - start) / 1_000_000);
		});
		
		
//		rateObservable.subscribe(
//					(result) -> LOGGER.info("Execution completed, exchange rate received: " + result),
//					(exception) -> LOGGER.info("Exception happened"),
//					() -> LOGGER.info("Completed")
//				);
		
		long end = System.nanoTime();
		LOGGER.info("OBSERVABLE execution time in millis: " + (end - start)
				/ 1_000_000);
	}
	
	public static void testObservable() throws InterruptedException
	{
		long start = System.nanoTime();
		
		Observable<ExchangeRate> exchangeRateObservable = new ObservableExchangeRateCommand().toObservable();
		exchangeRateObservable.take(5).subscribe(new Subscriber<ExchangeRate>()
		{
			@Override
			public void onNext(ExchangeRate exchangeRate)
			{
				LOGGER.info("USD to UAH: " + exchangeRate.getExchangeRate());
			}
			
			@Override
			public void onError(Throwable e)
			{
				LOGGER.info("Exception happened: " + e.getMessage());
			}
			
			@Override
			public void onCompleted()
			{
				LOGGER.info("COMPLETED");
			}
		});
		
		long end = System.nanoTime();
		LOGGER.info("STREAM execution time in millis: " + (end - start)
				/ 1_000_000);
	}
	
	public static void testCalculateUsdToEurExcahngeRateCollapsed()
			throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try
		{
			LOGGER.info("USD to EUR: " + ExchangeRateUtil.calculateUsdToEurExcahngeRateCollapsed());
		}
		finally
		{
			context.shutdown();
		}
		
		long end = System.nanoTime();
		LOGGER.info("COLLAPSED execution time in millis: " + (end - start)
				/ 1_000_000);
	}
}
