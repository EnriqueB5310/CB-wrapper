package com.CBHub.wrapper.services;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Component
public interface WeeklyComicService {
    List<Map<String, Object>> getWeeklyComics() throws NoSuchAlgorithmException;

}
