package com.microservices.hystrix.spring.integration.controller;

import com.microservices.model.ExchangeRate;
import com.microservices.model.PhonePrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<PhonePrice> getPhonePriceWithHystrix(String phoneModel) throws InterruptedException
	{
		BigDecimal phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		if(phonePriceInUSD == null)
		{
			return new ResponseEntity("Phone price not found.", HttpStatus.BAD_REQUEST);
		}

		ExchangeRate exchangeRate = exchangeRateServiceClient.getExchangeRate();
		BigDecimal priceInUAH = phonePriceInUSD.multiply(exchangeRate.getExchangeRate());
		priceInUAH.setScale(2, BigDecimal.ROUND_HALF_UP);
		return new ResponseEntity(new PhonePrice(phoneModel, phonePriceInUSD, priceInUAH), HttpStatus.OK);
	}
}
