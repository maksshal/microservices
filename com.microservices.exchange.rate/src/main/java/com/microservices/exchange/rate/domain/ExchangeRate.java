package com.microservices.exchange.rate.domain;

import java.text.DecimalFormat;

public class ExchangeRate {
	private String currencyToConvertTo;
	private String currencyToConvertFrom;
	private Double exchangeRate;

	public ExchangeRate(String currencyToConvertTo, String currencyToConvertFrom, Double exchangeRate) {
		super();
		this.currencyToConvertTo = currencyToConvertTo;
		this.currencyToConvertFrom = currencyToConvertFrom;
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyToConvertTo() {
		return currencyToConvertTo;
	}

	public String getCurrencyToConvertFrom() {
		return currencyToConvertFrom;
	}

	public String getExchangeRate() {
		return new DecimalFormat("###.##").format(exchangeRate);
	}

}
