package com.eureka.urllookup.urllookup.service;

import com.eureka.urllookup.urllookup.common.Constants;
import com.eureka.urllookup.urllookup.model.PrettyUrl;
import com.eureka.urllookup.urllookup.repository.URLMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(URLLookupCacheService.class);

    public URLLookupCacheService(RedisTemplate<String, String> redisTemplate) {
        valueOps = redisTemplate.opsForValue();
    }

    public String getPrettyURLFromLookup(HttpServletRequest request) {
        String requestURL = getRequestURL(request);
        String path = getValue(requestURL);
        if(path == null)
            path = bestMatch(requestURL);
        if(!path.equals(Constants.ERROR_NO_MATCH))
            return buildNewLink(request, path);
        return buildNewLink(request, request.getRequestURI());
    }

    private String buildNewLink(HttpServletRequest request, String path) {
        StringBuilder redirectURL = new StringBuilder(request.getRequestURL());
        redirectURL.delete(redirectURL.lastIndexOf("/"),redirectURL.length());
        redirectURL.append(path);
        return redirectURL.toString();
    }

    //returns cached value if present and checks db only in case of a cache miss which happens the first
    //time a url is accessed because cache has not been preloaded here.
    //Note: In a preloaded distributed cache, with replicas for DR, calls to db can completely be eliminated with redis'
    //persistence mechanism.
    private String getValue(String requestURL) {
        if(getCachedValue(requestURL)!=null)
            return getCachedValue(requestURL);
        return getValueFromDB(requestURL);
    }

    //only called the first time a request is made.
    private String getValueFromDB(String requestURL) {
        logger.info("Cache miss : looking up in db");
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
        logger.info("Finding best match");
        String prefix = url;
        String suffix = null;
        while(true){
            int lastParamIndex = prefix.lastIndexOf('&');
            if(lastParamIndex == -1){
                String urlMappedToPrefix = getValue(prefix);
                if(urlMappedToPrefix==null)
                    return Constants.ERROR_NO_MATCH;
                return urlMappedToPrefix+suffix;
            }
            suffix = prefix.substring(lastParamIndex+1);
            prefix = prefix.substring(0,lastParamIndex);
            String longestMappedValue = getValue(prefix);
            if(longestMappedValue!=null){
                return longestMappedValue+ "?" +suffix;
            }
        }
    }

    //Mapping path -> pretty and pretty -> path for lookups and reverse lookups
    public void cache(final String key, final String data) {
        valueOps.set(key, data);
        valueOps.set(data, key);
    }

    public String getCachedValue(final String key) {
        logger.info("Loading from cache");
        return valueOps.get(key);
    }

}
