package com.microservices.hystrix.spring.integration;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.microservices.hystrix.spring.integration.service.ExchangeRateConfiguration;

@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@RibbonClient(name = "exchange-rate", configuration = ExchangeRateConfiguration.class)
public class PhoneStoreApplication
{
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
    }
	
	public static void main(String[] args)
	{
		Locale.setDefault(Locale.US);
		SpringApplication.run(PhoneStoreApplication.class, args);
	}
}
