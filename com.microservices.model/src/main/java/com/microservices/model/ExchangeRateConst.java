package com.microservices.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateConst
{
    public static final String USD = "USD";
    public static final String EUR = "EUR";
    public static final String UAH = "UAH";

    public static final Map<String, BigDecimal> UAH_EXCHANGE_RATE_DEFAULT;
    static
    {
        Map<String, BigDecimal> defaults = new HashMap<>();
        defaults.put(USD, new BigDecimal("28.2"));
        defaults.put(EUR, new BigDecimal("31.0"));
        UAH_EXCHANGE_RATE_DEFAULT = Collections.unmodifiableMap(defaults);
    }
}
