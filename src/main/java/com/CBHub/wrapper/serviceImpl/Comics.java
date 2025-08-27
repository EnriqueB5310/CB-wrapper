package com.CBHub.wrapper.serviceImpl;

import com.CBHub.wrapper.exceptions.ComicNotFoundException;
import com.CBHub.wrapper.services.ComicService;
import com.CBHub.wrapper.util.md5Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class Comics implements ComicService {




    CacheManager cacheManager;

    md5Hasher hasher;

    private static final String BaseURL = "https://comicvine.gamespot.com/api/issues/";
    private static final String PublicKey = "2556a3bdb79e127ee06421ae41720c3a17a9bca7";
    private static  RestTemplate restTemplate;

    public Comics(md5Hasher hasher, CacheManager cacheManager, RestTemplate restTemplate) {

        this.cacheManager = cacheManager;
        this.hasher = hasher;
        this.restTemplate = restTemplate;
    }


    /**
     * @param id
     * @return
     * @throws NoSuchAlgorithmException
     */


    @Cacheable("Comic")
    @Override
    public Map<String, Object> getComic(Integer id) throws NoSuchAlgorithmException {

        if (id == null) throw new ComicNotFoundException();

        String url = String.format(
                "%s4000-%s/?api_key=%s&format=json",
                BaseURL, id, PublicKey
        );

       HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("User-Agent", "MyComicApp/1.0 (myemail@example.com)");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();


    }




    @Scheduled(cron = "0 0 01 * *") //midnight first of every month
    @CacheEvict(value="Comic", allEntries = true)
    public void clearComicCache() {};

}
