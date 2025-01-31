package com.CBHub.wrapper.payloads;

import com.CBHub.wrapper.services.FindHeroService;

import java.security.NoSuchAlgorithmException;
import java.util.Map;



public class Heros implements FindHeroService {
    @Override
    public Map<String, Object> findHero(String hero) throws NoSuchAlgorithmException {
        return Map.of();
    }
}
