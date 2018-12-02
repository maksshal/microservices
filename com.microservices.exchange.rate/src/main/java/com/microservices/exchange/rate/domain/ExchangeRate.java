package com.microservices.exchange.rate.domain;

import java.math.BigDecimal;

/**
 * Contains exchange rate for two currencies
 */
public class ExchangeRate {
	private String currencyToConvertTo;
	private String currencyToConvertFrom;
	private BigDecimal exchangeRate;

	public ExchangeRate(String currencyToConvertTo, String currencyToConvertFrom, BigDecimal exchangeRate)
	{
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

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

}
