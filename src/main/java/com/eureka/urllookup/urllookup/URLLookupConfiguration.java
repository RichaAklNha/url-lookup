package com.eureka.urllookup.urllookup;

import com.eureka.urllookup.urllookup.service.URLLookupCacheService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
@ConfigurationProperties
public class URLLookupConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());

        return new LettuceConnectionFactory(config);
    }

    @Bean({"redisTemplate"})
    public RedisTemplate<String, String> redisTemplate(RedisProperties redisProperties) {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(redisProperties));
        return redisTemplate;
    }

}
