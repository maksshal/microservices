package com.microservices.store.service;

import org.springframework.web.client.RestTemplate;

import rx.Observable;
import rx.Subscriber;

import com.microservices.store.domain.ExchangeRate;
import com.microservices.store.util.ExchangeRateUtil;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

public class ObservableExchangeRateCommand extends HystrixObservableCommand<ExchangeRate>
{
	public ObservableExchangeRateCommand()
	{
		super(
				Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ObservableExchangeRateCommand"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutEnabled(false))
			 );
    }
	
	@Override
	protected Observable<ExchangeRate> construct()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		return Observable.create(new Observable.OnSubscribe<ExchangeRate>()
		{
			@Override
			public void call(Subscriber<? super ExchangeRate> subscriber)
			{
				try
				{
					while(!subscriber.isUnsubscribed())
                	{
                		subscriber.onNext(restTemplate.getForObject("http://localhost:7001/getCurrentUAHExchangeRate?currency={currency}", ExchangeRate.class, ExchangeRateUtil.USD));
                		Thread.sleep(1_000);
                	}
					
					subscriber.onCompleted();
                }
				catch (Exception e)
				{
                    subscriber.onError(e);
                }
			}
         } );
	}
	
	@Override
	protected Observable<ExchangeRate> resumeWithFallback()
	{
		return Observable.create(new Observable.OnSubscribe<ExchangeRate>()
		{
			@Override
			public void call(Subscriber<? super ExchangeRate> subscriber)
			{
				try
				{
					while(!subscriber.isUnsubscribed())
                	{
						subscriber.onNext(new ExchangeRate("UAH", ExchangeRateUtil.USD, ExchangeRateUtil.UAH_EXCHANGE_RATE_DEFAULT.get(ExchangeRateUtil.USD)));
                		Thread.sleep(1_000);
                	}
                    subscriber.onCompleted();
                }
				catch (Exception e)
				{
                    subscriber.onError(e);
                }
			}
		});
	}
}
