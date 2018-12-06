package com.microservices.hystrix.spring.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.hystrix.spring.integration.domain.ExchangeRate;
import com.microservices.hystrix.spring.integration.domain.PhonePrice;
import com.microservices.hystrix.spring.integration.service.ExchangeRateServiceClient;
import com.microservices.hystrix.spring.integration.service.PhoneStoreRepo;

import java.math.BigDecimal;

@RestController
public class CalculatePriceController
{
	@Autowired
	private PhoneStoreRepo phoneStoreRepo;
	
	@Autowired
	private ExchangeRateServiceClient exchangeRateServiceClient;
	
	@RequestMapping(value = "getPhonePriceWithHystrix", method = RequestMethod.GET)
	public PhonePrice getPhonePriceWithHystrix(String phoneModel) throws InterruptedException
	{
		ExchangeRate exchangeRate = exchangeRateServiceClient.getExchangeRate();
		
		BigDecimal phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		BigDecimal priceInUAH = phonePriceInUSD.multiply(exchangeRate.getExchangeRate());
		priceInUAH.setScale(2, BigDecimal.ROUND_HALF_UP);
		return new PhonePrice(phoneModel, phonePriceInUSD, priceInUAH);
	}
}
