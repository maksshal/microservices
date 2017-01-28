package com.microservices.store.service;

import org.springframework.web.client.RestTemplate;

import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class ExchangeRateMicroserviceCommand extends HystrixCommand<Double>
{
	private String currency;
	
	public ExchangeRateMicroserviceCommand(String currency)
	{
		super(
				Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExchangeRateMicroservice"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerEnabled(true)
						.withCircuitBreakerRequestVolumeThreshold(10)
						.withCircuitBreakerErrorThresholdPercentage(50)
						.withCircuitBreakerSleepWindowInMilliseconds(5000)
						
						.withExecutionTimeoutInMilliseconds(5_000)
						
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(100)
						
						.withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
//						.withExecutionIsolationSemaphoreMaxConcurrentRequests(100)
					)
					.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withAllowMaximumSizeToDivergeFromCoreSize(true)
						.withMaximumSize(100)
					)
					
			 );
		
		this.currency = currency;
	}

	@Override
	protected Double run() throws Exception
	{
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.getForObject("http://localhost:7001/getCurrentUSDollarExchangeRate?currency={currency}", Double.class, currency);
	}

	@Override
	protected Double getFallback()
	{
		return ExchangeRateUtil.EXCHANGE_RATE_DEFAULT;
	}
	
	@Override
	protected String getCacheKey() {
		return currency;
	}
}
