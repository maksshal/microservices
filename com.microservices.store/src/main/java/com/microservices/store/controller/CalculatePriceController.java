package com.microservices.store.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.domain.PhonePrice;
import com.microservices.store.service.ExchangeRateMicroserviceCommand;
import com.microservices.store.service.ExchangeRateMicroserviceCommandSimple;
import com.microservices.store.service.PhoneStoreRepo;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@RestController
public class CalculatePriceController
{
	private static final Logger LOGGER = Logger.getLogger(CalculatePriceController.class);
	
	@Autowired
	private PhoneStoreRepo phoneStoreRepo;

	/**
	 * Get phone price without using Hystrix
	 * @param phoneModel
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "getPhonePrice", method = RequestMethod.GET)
	public PhonePrice getPhonePrice(String phoneModel) throws InterruptedException
	{
		RestTemplate restTemplate = new RestTemplate();
		double exchangeRate;
		try
		{
			ExchangeRate exchangeRateResponseEntity = restTemplate.getForObject(ExchangeRateUtil.EXCHANGE_RATE_SERVICE_URL, ExchangeRate.class, "USD");
			exchangeRate = exchangeRateResponseEntity.getExchangeRate();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			exchangeRate = ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get("USD");
		}
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRate);
	}

	/**
	 * Get phone price using Hystrix fallbacks
	 * @param phoneModel
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "getPhonePriceWithHystrix", method = RequestMethod.GET)
	public PhonePrice getPhonePriceWithHystrix(String phoneModel) throws InterruptedException
	{
		ExchangeRateMicroserviceCommandSimple exchangeRateMicroserviceCommand = new ExchangeRateMicroserviceCommandSimple("USD");
		ExchangeRate exchangeRate = exchangeRateMicroserviceCommand.execute();
		
		double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
		return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRate.getExchangeRate());
	}

	/**
	 * Retrieve phone price in UAH after converting it from USD, using Hystrix async execution, logging and caching
	 * @param phoneModel
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "getPhonePriceWithHystrixAdvanced", method = RequestMethod.GET)
	public PhonePrice getPhonePriceWithHystrixAdvanced(String phoneModel) throws InterruptedException, ExecutionException
	{
		HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
		
		try
		{
			ExchangeRateMicroserviceCommand exchangeRateMicroserviceCommand = new ExchangeRateMicroserviceCommand("USD");

			//Here microservice call will be non-blocking...
			Future<ExchangeRate> exchangeRate = exchangeRateMicroserviceCommand.queue();
			//...so while it is executing, we may retrieve some info from, lets say, database...
			double phonePriceInUSD = phoneStoreRepo.getPhonePriceInUSD(phoneModel);
			//...and only then block
			ExchangeRate exchangeRateResponse = exchangeRate.get();
			
			if(exchangeRateMicroserviceCommand.isResponseFromFallback())
			{
				if(exchangeRateMicroserviceCommand.isResponseShortCircuited())
				{
					LOGGER.info("CIRCUIT OPENED, so calling fallback immediately.");
				}
				else if(exchangeRateMicroserviceCommand.isResponseTimedOut())
				{
					LOGGER.info("Hystrix: request timeout.");
				}
				else
				{
					LOGGER.info("Hystrix: executing fallback.");
				}
			}
			else
			{
				LOGGER.info("Hystrix: request executed successfully.");
			}

			//using this call just to make sure that we reused value from a cache, since this request is executed
			//in the same Hystrix context
			LOGGER.info("USD to EUR rate: " + ExchangeRateUtil.calculateUsdToEurExchangeRate());

			return new PhonePrice(phoneModel, phonePriceInUSD, phonePriceInUSD * exchangeRateResponse.getExchangeRate());
		}
		finally
		{
			hystrixRequestContext.shutdown();
		}
	}
}
