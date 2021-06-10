package com.eureka.urllookup.urllookup.controller;

import com.eureka.urllookup.urllookup.service.URLLookupCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class URLController {
    @Autowired
    private URLLookupCacheService urlLookupCacheService;

    //All path urls are intercepted here and matching pretty urls are returned in response
    @GetMapping("/products")
    @ResponseBody
    public void getURLMapping(@RequestParam(required = false) String brand,
                              @RequestParam(required = false) String gender,
                              @RequestParam(required = false) String tag,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mappedURL = urlLookupCacheService.getPrettyURLFromLookup(request);
        response.sendRedirect(mappedURL);
    }

    // Temporary workaround for favicon.ico requests.
    @GetMapping("/favicon.ico")
    public ResponseEntity.BodyBuilder ignore(HttpServletRequest request) {
        return ResponseEntity.accepted();
    }
}
