package com.microservices.store.service;

import java.net.URI;

import org.springframework.web.client.RestTemplate;

import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class ExchangeRateMicroserviceCommand extends HystrixCommand<Double>
{
	public ExchangeRateMicroserviceCommand()
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
	}

	@Override
	protected Double run() throws Exception
	{
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.getForObject(new URI("http://localhost:7001/getCurrentUSDollarExchangeRate"), Double.class);
	}

	@Override
	protected Double getFallback()
	{
		return ExchangeRateUtil.EXCHANGE_RATE_DEFAULT;
	}
}
