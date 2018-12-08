package com.microservices.hystrix.spring.integration.service;

import com.microservices.model.PhonePriceDefaults;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Repository mock for storing phone prices in USD
 */
@Repository
public class PhoneStoreRepo
{
	public BigDecimal getPhonePriceInUSD(String phoneModel) throws InterruptedException
	{
//		Thread.sleep(10_000);
		return PhonePriceDefaults.PHONE_PRICES.get(phoneModel);
	}
}
