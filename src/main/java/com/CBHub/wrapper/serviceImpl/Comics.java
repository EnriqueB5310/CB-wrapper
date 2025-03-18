package com.CBHub.wrapper.serviceImpl;

import com.CBHub.wrapper.exceptions.ComicNotFoundException;
import com.CBHub.wrapper.services.ComicService;
import com.CBHub.wrapper.util.md5Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class Comics implements ComicService {



    @Autowired
    CacheManager cacheManager;

    md5Hasher hasher;
    private static final String BaseURL = "https://gateway.marvel.com/v1/public/comics";
    private static final String PublicKey = "23bcbeb0ba49ee64a339eae3329ad658";
    private static final String PrivateKey = "16d2a0d1717b7b50c601570e495512d7d9474508";


    public Comics(md5Hasher hasher) {
        this.hasher = hasher;
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

        String timestamp = String.valueOf(System.currentTimeMillis());
        String input = timestamp + PrivateKey + PublicKey;
        String hash = md5Hasher.getMd5(input);

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format( "%s/%d?ts=%s&apikey=%s&hash=%s",
                BaseURL, id, timestamp, PublicKey, hash);

        return restTemplate.getForObject(url,Map.class);


    }




    @Scheduled(cron = "0 0 01 * *") //midnight first of every month
    @CacheEvict(value="Comic", allEntries = true)
    public void clearComicCache() {};

}
