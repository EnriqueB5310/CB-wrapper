package com.CBHub.wrapper.services;
import java.security.NoSuchAlgorithmException;

import java.util.Map;

public interface FindHeroService {
    //finds specific hero metadata
    public Map<String,Object> findHero(String hero) throws NoSuchAlgorithmException;

    }


