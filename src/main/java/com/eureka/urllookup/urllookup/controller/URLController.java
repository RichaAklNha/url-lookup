package com.eureka.urllookup.urllookup.controller;

import com.eureka.urllookup.urllookup.service.URLLookupCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class URLController {
    @Autowired
    private URLLookupCacheService urlLookupCacheService;

    @GetMapping(value="**")
    public ResponseEntity<String> getURLMapping(HttpServletRequest request) {
        String mappedURL = urlLookupCacheService.getAlternateURLFromLookup(request);
        return ResponseEntity.ok(mappedURL);
    }

    @GetMapping(value="/favicon.ico")
    public ResponseEntity.BodyBuilder ignore(HttpServletRequest request) {
        return ResponseEntity.accepted();
    }
}
