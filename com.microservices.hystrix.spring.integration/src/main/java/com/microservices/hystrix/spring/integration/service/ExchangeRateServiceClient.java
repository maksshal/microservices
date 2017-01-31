package com.microservices.hystrix.spring.integration.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservices.hystrix.spring.integration.domain.ExchangeRate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class ExchangeRateServiceClient
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateServiceClient.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "defaultExchangeRate",
			commandProperties = {@HystrixProperty(name="execution.isolation.strategy", value="THREAD")})
    public ExchangeRate getExchangeRate()
	{
		ExchangeRate exchangeRateResponseEntity = restTemplate.getForObject("http://exchange-rate/getCurrentUAHExchangeRate?currency={currency}", ExchangeRate.class, "USD");
		return exchangeRateResponseEntity;
    }
 
    public ExchangeRate defaultExchangeRate()
    {
    	LOGGER.info("Executing fallback");
        return new ExchangeRate("UAH", "USD", 22.7);
    }
}
