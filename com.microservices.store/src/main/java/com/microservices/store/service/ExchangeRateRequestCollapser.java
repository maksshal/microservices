package com.microservices.store.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.microservices.model.ExchangeRate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import static com.microservices.model.ExchangeRateConst.*;

/**
 * Collapser which will collect all requests during a time window and execute only one HTTP call instead
 */
public class ExchangeRateRequestCollapser extends HystrixCollapser<Map<String, ExchangeRate>, ExchangeRate, String>
{
	private String currency;

	public ExchangeRateRequestCollapser(String currency)
	{
		super(HystrixCollapser.Setter
				.withCollapserKey(HystrixCollapserKey.Factory.asKey("ExchangeRateRequestCollapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(500)));
		this.currency = currency;
	}

	@Override
	public String getRequestArgument()
	{
		return currency;
	}

	@Override
	protected HystrixCommand<Map<String, ExchangeRate>> createCommand(
			Collection<CollapsedRequest<ExchangeRate, String>> requests)
	{
		return new ExchangeRateBatchCommand(requests);
	}

	@Override
	protected void mapResponseToRequests(
			Map<String, ExchangeRate> batchResponse,
			Collection<CollapsedRequest<ExchangeRate, String>> requests)
	{
        for (CollapsedRequest<ExchangeRate, String> request : requests)
        {
            request.setResponse(batchResponse.get(request.getArgument()));
        }
	}

	/**
	 * Command which contains logic how to collapse several similar requests to one HTTP request
	 */
	private class ExchangeRateBatchCommand extends HystrixCommand<Map<String, ExchangeRate>>
	{
        private final Collection<CollapsedRequest<ExchangeRate, String>> requests;

        private ExchangeRateBatchCommand(Collection<CollapsedRequest<ExchangeRate, String>> requests)
        {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExchangeRateBatchCommand")));
            
            this.requests = requests;
        }

        @Override
        protected Map<String, ExchangeRate> run()
        {
            String currencies = requests.stream()
					            	.map(CollapsedRequest::getArgument)
					            	.collect(Collectors.joining(","));
            
            RestTemplate restTemplate = new RestTemplate();
    		ResponseEntity<ExchangeRate[]> ratesResponse = restTemplate.getForEntity("http://localhost:7001/getCurrentUAHExchangeRateForCurrencies?currencies={currencies}", ExchangeRate[].class, currencies);
    		ExchangeRate[] rates = ratesResponse.getBody();
    		
    		Map<String, ExchangeRate> response =
    			    Arrays.stream(rates).collect(Collectors.toMap(ExchangeRate::getCurrencyToConvertFrom, rate -> rate));
            
            return response;
        }
        
        @Override
        protected Map<String, ExchangeRate> getFallback()
        {
        	Map<String, ExchangeRate> response = new HashMap<>();
        	response.put(USD, new ExchangeRate(UAH, USD, UAH_EXCHANGE_RATE_DEFAULT.get(USD)));
        	response.put(EUR, new ExchangeRate(UAH, EUR, UAH_EXCHANGE_RATE_DEFAULT.get(EUR)));
        	return response;
        }
    }

}
