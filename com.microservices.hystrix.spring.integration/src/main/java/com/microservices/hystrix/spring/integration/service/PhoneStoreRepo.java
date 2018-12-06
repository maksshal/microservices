package com.microservices.hystrix.spring.integration.service;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository mock for storing phone prices in USD
 */
@Repository
public class PhoneStoreRepo
{
	private static final Map<String, BigDecimal> PHONE_PRICES = new HashMap<>();
	static
	{
		PHONE_PRICES.put("Huawei GR5", new BigDecimal("200.0"));
		PHONE_PRICES.put("Huawei Y6 Pro", new BigDecimal("150.0"));
		PHONE_PRICES.put("Huawei P9 Prestige", new BigDecimal("666.66"));
		
		PHONE_PRICES.put("Meizu M2", new BigDecimal("100.0"));
		PHONE_PRICES.put("Meizu U10", new BigDecimal("200.0"));
		PHONE_PRICES.put("Meizu Pro 6", new BigDecimal("300.0"));
	}
	
	public BigDecimal getPhonePriceInUSD(String phoneModel) throws InterruptedException
	{
//		Thread.sleep(10_000);
		return PHONE_PRICES.get(phoneModel);
	}
}
