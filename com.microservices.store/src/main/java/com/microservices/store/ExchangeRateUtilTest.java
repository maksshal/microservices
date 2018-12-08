package com.microservices.store;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import com.microservices.model.ExchangeRate;
import org.apache.log4j.Logger;

import rx.Observable;
import rx.Subscriber;

import com.microservices.store.service.ObservableExchangeRateCommand;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * Execute Hystrix command in different ways: sync, async, via observable etc.
 */
public class ExchangeRateUtilTest
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateUtilTest.class);

	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		testCalculateUsdToEurExcahngeRate();
		testCalculateUsdToEurExcahngeRateAsync();
		testCalculateUsdToEurExcahngeRateHotObservable();
		testCalculateUsdToEurExchangeRateCollapsed();
		testObservable();
	}

	/**
	 * Calculate execution time of two sequential requests
	 */
	private static void testCalculateUsdToEurExcahngeRate()
	{
		long start = System.nanoTime();
		BigDecimal result = ExchangeRateUtil.calculateUsdToEurExchangeRate();
		long end = System.nanoTime();
		LOGGER.info("SYNC execution time in millis: " + (end - start)
				/ 1_000_000 + " Result: " + result);
	}

	/**
	 * Calculate execution time of two parallel requests
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private static void testCalculateUsdToEurExcahngeRateAsync()
			throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		BigDecimal result = ExchangeRateUtil.calculateUsdToEurExchangeRateAsync();
		long end = System.nanoTime();
		LOGGER.info("ASYNC execution time in millis: " + (end - start)
				/ 1_000_000 + " Result: " + result);
	}

	/**
	 * Call Hystrix using Observable
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private static void testCalculateUsdToEurExcahngeRateHotObservable() throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		
		Observable<BigDecimal> rateObservable = ExchangeRateUtil.calculateUsdToEurExchangeRateHotObservable();
		rateObservable.subscribe(
					(result) ->
					{
						LOGGER.info("Execution COMPLETED, exchange rate received: " + result);
						LOGGER.info("Time elapsed: " + (System.nanoTime() - start) / 1_000_000);
					},
					(exception) -> LOGGER.error("Exception happened", exception),
					() -> LOGGER.info("Completed")
				);

		long end = System.nanoTime();
		LOGGER.info("Time to create observable in millis: " + (end - start)
				/ 1_000_000);
		Thread.sleep(2000);
	}

	/**
	 * Process data stream from microservice
	 * @throws InterruptedException
	 */
	private static void testObservable() throws InterruptedException
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

	/**
	 * Calculate execution time of collapsed requests
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private static void testCalculateUsdToEurExchangeRateCollapsed()
			throws InterruptedException, ExecutionException
	{
		long start = System.nanoTime();
		
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try
		{
			LOGGER.info("USD to EUR: " + ExchangeRateUtil.calculateUsdToEurExchangeRateCollapsed());
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
