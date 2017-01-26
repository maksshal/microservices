package com.microservices.store.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PhoneStoreRepo
{
	private static final Map<String, Double> PHONE_PRICES = new HashMap<>();
	static
	{
		PHONE_PRICES.put("Huawei GR5", 200.0);
		PHONE_PRICES.put("Huawei Y6 Pro", 150.0);
		PHONE_PRICES.put("Huawei P9 Prestige", 666.66);
		
		PHONE_PRICES.put("Meizu M2", 100.0);
		PHONE_PRICES.put("Meizu U10", 200.0);
		PHONE_PRICES.put("Meizu Pro 6", 300.0);
	}
	
	public double getPhonePriceInUSD(String phoneModel)
	{
		return PHONE_PRICES.get(phoneModel);
	}
}
