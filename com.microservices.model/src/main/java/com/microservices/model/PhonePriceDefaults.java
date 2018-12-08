package com.microservices.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PhonePriceDefaults
{
    public static final Map<String, BigDecimal> PHONE_PRICES;
    static
    {
        Map<String, BigDecimal> phonePrices = new HashMap<>();

        phonePrices.put("Huawei GR5", new BigDecimal("200.0"));
        phonePrices.put("Huawei Y6 Pro", new BigDecimal("150.0"));
        phonePrices.put("Huawei P9 Prestige", new BigDecimal("666.66"));

        phonePrices.put("Meizu M2", new BigDecimal("100.0"));
        phonePrices.put("Meizu U10", new BigDecimal("200.0"));
        phonePrices.put("Meizu Pro 6", new BigDecimal("300.0"));

        PHONE_PRICES = Collections.unmodifiableMap(phonePrices);
    }
}
