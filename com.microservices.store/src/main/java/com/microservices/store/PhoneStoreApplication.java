package com.microservices.store;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhoneStoreApplication
{
	public static void main(String[] args)
	{
		Locale.setDefault(Locale.US);
		SpringApplication.run(PhoneStoreApplication.class, args);
	}
}
