package com.microservices.store.domain;

import java.math.BigDecimal;

public class ExchangeRate
{
	private String currencyToConvertTo;
	private String currencyToConvertFrom;
	private BigDecimal exchangeRate;

	public ExchangeRate()
	{
	}

	public ExchangeRate(String currencyToConvertTo, String currencyToConvertFrom, BigDecimal exchangeRate)
	{
		this.currencyToConvertTo = currencyToConvertTo;
		this.currencyToConvertFrom = currencyToConvertFrom;
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyToConvertTo() {
		return currencyToConvertTo;
	}

	public void setCurrencyToConvertTo(String currencyToConvertTo) {
		this.currencyToConvertTo = currencyToConvertTo;
	}

	public String getCurrencyToConvertFrom() {
		return currencyToConvertFrom;
	}

	public void setCurrencyToConvertFrom(String currencyToConvertFrom) {
		this.currencyToConvertFrom = currencyToConvertFrom;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
