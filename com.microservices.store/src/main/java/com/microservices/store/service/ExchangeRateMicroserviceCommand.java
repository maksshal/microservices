package com.microservices.store.service;

import org.springframework.web.client.RestTemplate;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class ExchangeRateMicroserviceCommand extends HystrixCommand<ExchangeRate>
{
	private String currency;
	
	public ExchangeRateMicroserviceCommand(String currency)
	{
		super(
				Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExchangeRateMicroservice"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerEnabled(true)
						.withCircuitBreakerRequestVolumeThreshold(10)		//for every 10 requests
						.withCircuitBreakerErrorThresholdPercentage(50)		//if 50% of them fail, open a circuit
						.withCircuitBreakerSleepWindowInMilliseconds(5000)	//and keep it opened for 5 secs before retry
						
						.withExecutionTimeoutInMilliseconds(5_000)			//if call runs longer, fallback will be executed
						
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(100)	//if more requests are executed simultaneously, fallback will happen
						
						.withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)	//run every request in a separate thread
					)
					.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withAllowMaximumSizeToDivergeFromCoreSize(true)
						.withMaximumSize(100)	//thread pool size
					)
					
			 );
		
		this.currency = currency;
	}

	@Override
	protected ExchangeRate run()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.getForObject(ExchangeRateUtil.EXCHANGE_RATE_SERVICE_URL, ExchangeRate.class, currency);
	}

	@Override
	protected ExchangeRate getFallback()
	{
		return new ExchangeRate("UAH", currency, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(currency));
	}

	//Enable this if we want to use caching.
//	@Override
//	protected String getCacheKey()
//	{
//		return currency;
//	}
}
