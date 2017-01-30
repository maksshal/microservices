package com.microservices.hystrix.spring.integration.domain;

import java.text.DecimalFormat;

public class PhonePrice {
	private String modelName;
	private Double priceInUSD;
	private Double priceInUAH;

	public PhonePrice(String modelName, Double priceInUSD, Double priceInUAH) {
		super();
		this.modelName = modelName;
		this.priceInUSD = priceInUSD;
		this.priceInUAH = priceInUAH;
	}

	public String getModelName() {
		return modelName;
	}

	public String getPriceInUSD() {
		return new DecimalFormat("###.##").format(priceInUSD);
	}

	public String getPriceInUAH() {
		return new DecimalFormat("###.##").format(priceInUAH);
	}

}
