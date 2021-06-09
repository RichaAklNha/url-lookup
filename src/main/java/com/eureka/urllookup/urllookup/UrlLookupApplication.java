package com.eureka.urllookup.urllookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties({URLLookupConfiguration.class,
								RedisProperties.class})
@EnableJpaRepositories
@EntityScan
public class UrlLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlLookupApplication.class, args);
	}

}
