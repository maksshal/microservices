# microservices
Presentation preparation for Hystrix and microservices:
- **JMeterSuite**: JMeter project for microservices load testing.
- **com.microservices.exchange.rate**: microservice which will return random currency rate for UAH/USD and UAG/EUR.
- **com.microservices.store**: phone store application, which will contact exchange.rate microservice to get up-to-date exchange rates. Wraps all calls using Hystrix.
- **com.microservices.hystrix.spring.integration**: the same store application but using Spring Cloud features: Hystrix annotations, service discovery.
- **eureka**: Eureka server for microservice registration and discovery.
