package com.eureka.urllookup.urllookup.service;

import com.eureka.urllookup.urllookup.model.PrettyUrl;
import com.eureka.urllookup.urllookup.repository.URLMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@CacheConfig(cacheNames = "url_map")
public class URLLookupCacheService {

    @Autowired
    private URLMapRepository urlMapRepository;

    private final ValueOperations<String, String> valueOps;

    public URLLookupCacheService(RedisTemplate<String, String> redisTemplate) {
        valueOps = redisTemplate.opsForValue();
    }

    public String getAlternateURLFromLookup(HttpServletRequest request) {
        String requestURL = getRequestURL(request);
        String exactMatch = getValue(requestURL);
        if(exactMatch == null)
            return bestMatch((requestURL));
        else
            return exactMatch;
    }

    private String getValue(String requestURL) {
        if(getCachedValue(requestURL)!=null)
            return getCachedValue(requestURL);
        else {
            return getValueFromDB(requestURL);
        }
    }

    private String getValueFromDB(String requestURL) {
        List<PrettyUrl> prettyUrlList = urlMapRepository.findURLMap(requestURL);
        if(prettyUrlList.size()==0)
            return null;
        PrettyUrl prettyUrl = prettyUrlList.get(0);
        cache(prettyUrl.getPath(), prettyUrl.getPretty());
        return getCachedValue(requestURL);
    }

    private String getRequestURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();

        if (queryString == null) {
            requestURL.toString();
        } else {
            requestURL.append('?').append(queryString).toString();
        }
        return requestURL.toString();
    }

    private String bestMatch(String url) {
        String prefix = url;
        String suffix = null;
        while(true){
            int lastParamIndex = prefix.lastIndexOf('&');
            if(lastParamIndex == -1){
                if(getValue(prefix)==null)
                    return "No mapping found";
                else
                    return getValue(prefix)+suffix;
            }
            suffix = prefix.substring(lastParamIndex+1);
            prefix = prefix.substring(0,lastParamIndex);
            String longestMappedValue = getValue(prefix);
            if(longestMappedValue!=null){
                return longestMappedValue+ "?" +suffix;
            }
        }
    }

    public void cache(final String key, final String data) {
        valueOps.set(key, data);
        valueOps.set(data, key);
    }

    public String getCachedValue(final String key) {
        return valueOps.get(key);
    }

}
