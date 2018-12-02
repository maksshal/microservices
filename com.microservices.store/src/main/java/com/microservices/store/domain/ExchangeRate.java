package com.microservices.store.domain;

public class ExchangeRate
{
	private String currencyToConvertTo;
	private String currencyToConvertFrom;
	private Double exchangeRate;

	public ExchangeRate()
	{
	}

	public ExchangeRate(String currencyToConvertTo, String currencyToConvertFrom, Double exchangeRate)
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

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	

}
