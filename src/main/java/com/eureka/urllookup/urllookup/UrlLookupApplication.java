package com.eureka.urllookup.urllookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({URLLookupConfiguration.class,
								RedisProperties.class})
public class UrlLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlLookupApplication.class, args);
	}

}
