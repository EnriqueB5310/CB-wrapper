package com.CBHub.wrapper.services;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface ComicService {
//using interger to check for null values in method param
    public Map<String,Object> getComic(Integer id) throws NoSuchAlgorithmException;
}
