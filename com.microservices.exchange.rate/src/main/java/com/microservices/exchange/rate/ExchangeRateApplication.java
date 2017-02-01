package com.microservices.exchange.rate;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class ExchangeRateApplication
{
	public static void main(String[] args)
	{
		Locale.setDefault(Locale.US);
		SpringApplication.run(ExchangeRateApplication.class, args);
	}
}
