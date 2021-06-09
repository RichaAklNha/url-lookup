package com.eureka.urllookup.urllookup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("redis")
@Getter
@AllArgsConstructor
public class RedisProperties {
    private final String host;
    private final int port;
}
