package com.microservices.hystrix.spring.integration.service;

import com.microservices.model.ExchangeRate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.math.BigDecimal;

@Service
public class ExchangeRateServiceClient
{
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateServiceClient.class);
	public static final String EXCHANGE_RATE_ENDPOINT = "http://exchange-rate/getCurrentUAHExchangeRate?currency={currency}";

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "defaultExchangeRate",
			commandProperties = {@HystrixProperty(name="execution.isolation.strategy", value="THREAD")})
	public ExchangeRate getExchangeRate()
	{
		ExchangeRate exchangeRateResponseEntity = restTemplate.getForObject(EXCHANGE_RATE_ENDPOINT, ExchangeRate.class, "USD");
		return exchangeRateResponseEntity;
	}
 
	public ExchangeRate defaultExchangeRate()
	{
		LOGGER.info("Executing fallback");
		return new ExchangeRate("UAH", "USD", new BigDecimal("28.1"));
	}
}
