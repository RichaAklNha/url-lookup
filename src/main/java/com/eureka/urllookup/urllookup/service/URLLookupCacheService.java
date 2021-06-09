package com.eureka.urllookup.urllookup.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class URLLookupCacheService {

    private final ValueOperations<String, String> valueOps;

    public URLLookupCacheService(RedisTemplate<String, String> redisTemplate) {
        valueOps = redisTemplate.opsForValue();
    }

    public String getAlternateURLFromLookup(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();

        if (queryString == null) {
            requestURL.toString();
        } else {
            requestURL.append('?').append(queryString).toString();
        }

        if(getCachedValue(requestURL.toString())!=null)
            return getCachedValue(requestURL.toString());
        else {
            return bestMatch(requestURL.toString());
        }
    }

    private String bestMatch(String url) {
        return url;
    }

    public void cache(final String key, final String data) {
        valueOps.set(key, data);
    }

    public String getCachedValue(final String key) {
        return valueOps.get(key);
    }

    public void deleteCachedValue(final String key) {
        valueOps.getOperations().delete(key);
    }
}
