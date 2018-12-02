package com.microservices.store.service;

import org.springframework.web.client.RestTemplate;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

/**
 * Exchange rate command with semaphore isolation
 */
public class ExchangeRateMicroserviceCommandSimple extends HystrixCommand<ExchangeRate>
{
	private String currency;
	
	public ExchangeRateMicroserviceCommandSimple(String currency)
	{
		super(
				Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExchangeRateMicroservice"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(100)	//more concurrent fallbacks will cause exception
						
						.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
						.withExecutionIsolationSemaphoreMaxConcurrentRequests(100)
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
}
