package com.microservices.store.service;

import java.math.BigDecimal;

import com.microservices.model.PhonePriceDefaults;
import org.springframework.stereotype.Repository;

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
