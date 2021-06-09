package com.eureka.urllookup.urllookup.controller;

import com.eureka.urllookup.urllookup.service.URLLookupCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class URLController {
    @Autowired
    private URLLookupCacheService urlLookupCacheService;

    @GetMapping(value="**")
    public String getMapping(HttpServletRequest request) {
        return urlLookupCacheService.getAlternateURLFromLookup(request);
    }


}
