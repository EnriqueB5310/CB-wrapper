package com.CBHub.wrapper.services;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public interface MonthlyComicService {
    public Map<String,Object> getMonthlyComics() throws NoSuchAlgorithmException;
}
