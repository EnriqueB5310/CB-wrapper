package com.CBHub.wrapper.controller;


import com.CBHub.wrapper.serviceImpl.Comics;
import com.CBHub.wrapper.serviceImpl.WeeklyComics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private WeeklyComics service;

    @Autowired
    private Comics comicService;

    public ComicController(WeeklyComics service, Comics comicService) {
        this.service = service;
        this.comicService = comicService;
    }

    /**
     * Endpoint to fetch weekly releases as of request
     * http://localhost:8080/comics/this-week
     * @return JSON response of weekly comics from marvel API
     */

    @GetMapping("/this-week")
    public Map<String, Object> getThisWeek() throws NoSuchAlgorithmException {
        return service.getWeeklyComics();
    }


    /**
     * http://localhost:8080/comics/comic?id=1122921
     * @param id
     * @return
     * @throws NoSuchAlgorithmException
     */
    @GetMapping("/comic")
    @ResponseBody
    public Map<String,Object> getComic(@RequestParam int id) throws NoSuchAlgorithmException {


        return comicService.getComic(id);
    }

}
