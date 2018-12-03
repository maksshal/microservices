package com.microservices.store.domain;

import java.math.BigDecimal;

public class PhonePrice
{
	private String modelName;
	private BigDecimal priceInUSD;
	private BigDecimal priceInUAH;

	public PhonePrice(String modelName, BigDecimal priceInUSD, BigDecimal priceInUAH)
	{
		this.modelName = modelName;
		this.priceInUSD = priceInUSD;
		this.priceInUAH = priceInUAH;
	}

	public String getModelName() {
		return modelName;
	}

	public BigDecimal getPriceInUSD() {
		return priceInUSD;
	}

	public BigDecimal getPriceInUAH() {
		return priceInUAH;
	}
}
