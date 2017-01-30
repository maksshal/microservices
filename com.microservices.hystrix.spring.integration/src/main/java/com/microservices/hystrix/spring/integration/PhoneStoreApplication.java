package com.microservices.hystrix.spring.integration;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrix
@EnableCircuitBreaker
@EnableHystrixDashboard
public class PhoneStoreApplication
{
	public static void main(String[] args)
	{
		Locale.setDefault(Locale.US);
		SpringApplication.run(PhoneStoreApplication.class, args);
	}
}
