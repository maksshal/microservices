package com.microservices.hystrix.spring.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.hystrix.spring.integration.domain.ExchangeRate;
import com.microservices.hystrix.spring.integration.domain.PhonePrice;
import com.microservices.hystrix.spring.integration.service.ExchangeRateServiceClient;
import com.microservices.hystrix.spring.integration.service.PhoneStoreRepo;

@RestController
public class CalculatePriceController
{
	@Autowired
	private PhoneStoreRepo phoneStoreRepo;
	
	@Autowired
	private ExchangeRateServiceClient exchangeRateServiceClient;
	
	@RequestMapping(value = "getPhonePrice", method = RequestMethod.GET)
	public PhonePrice getPhonePrice(String phoneModel) throws InterruptedException
	{
		ExchangeRate exchangeRate = exchangeRateServiceClient.getExchangeRate();
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRate.getExchangeRate());
	}
}
