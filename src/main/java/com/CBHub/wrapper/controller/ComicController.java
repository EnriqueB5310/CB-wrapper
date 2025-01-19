package com.CBHub.wrapper.controller;


import com.CBHub.wrapper.payloads.WeeklyComics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private WeeklyComics service;

    /**
     * Endpoint to fetch weekly releases as of request
     *
     * @return JSON response of weekly comics from marvel API
     */

    @GetMapping("/this-week")
    public Map<String, Object> getThisWeek() throws NoSuchAlgorithmException {
        return service.getWeeklyComics();
    }

}
