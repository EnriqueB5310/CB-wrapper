package com.CBHub.wrapper.payloads;


//utility class for making direct calls to marvel API

import com.CBHub.wrapper.services.WeeklyComicService;
import com.CBHub.wrapper.util.md5Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class WeeklyComics implements WeeklyComicService {

@Autowired
    CacheManager cacheManager;


    md5Hasher hasher;

    private static final String BaseURL = "https://gateway.marvel.com/v1/public/comics";
    private static final String PublicKey = "23bcbeb0ba49ee64a339eae3329ad658";
    private static final String PrivateKey = "16d2a0d1717b7b50c601570e495512d7d9474508";


    /**
     * Fetches weekly comics from marvel API.
     * results are cached
     *
     * @return JSON response from the marvel API containing the weekly releases of comics
     */

    @Override
    @Cacheable(value="thisWeeksComics")
    public Map<String,Object> getWeeklyComics() throws NoSuchAlgorithmException {

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateRange = weekStart.format(formatter) + "," + weekEnd.format(formatter);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String input = timestamp + PrivateKey + PublicKey;
        String hash = md5Hasher.getMd5(input);

        String url = String.format(
                "%s?dateRange=%s&orderBy=onsaleDate&apikey=%s&ts=%s&hash=%s",
                BaseURL, dateRange, PublicKey, timestamp, hash
        );

        RestTemplate restTemplate = new RestTemplate();

        //fetch response and convert to map
        return restTemplate.getForObject(url,Map.class);

    }


}
