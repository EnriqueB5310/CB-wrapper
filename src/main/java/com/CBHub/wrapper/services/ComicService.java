package com.CBHub.wrapper.services;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface ComicService {

    public Map<String,Object> getComic(int id) throws NoSuchAlgorithmException;
}
