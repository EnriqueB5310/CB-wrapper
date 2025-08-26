package com.CBHub.wrapper.serviceImpl;


//utility class for making direct calls to marvel API

import com.CBHub.wrapper.exceptions.BadUrlException;
import com.CBHub.wrapper.services.WeeklyComicService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class WeeklyComics implements WeeklyComicService {


    CacheManager cacheManager;


    md5Hasher hasher;

    private static final String BaseURL = "https://comicvine.gamespot.com/api/issues/";
    private static final String PublicKey = "2556a3bdb79e127ee06421ae41720c3a17a9bca7";



    public WeeklyComics(md5Hasher hasher, CacheManager cacheManager) {

        this.cacheManager = cacheManager;
        this.hasher = hasher;
    }

    /**
     * Fetches weekly comics from marvel API.
     * results are cached
     *
     * @return JSON response from the marvel API containing the weekly releases of comics
     */

    @Override
    @Cacheable(value = "thisWeeksComics")
    public Map<String, Object> getWeeklyComics() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateRange = weekStart.format(formatter) + "|" + weekEnd.format(formatter);

        String url = String.format(
                "%s?api_key=%s&format=json&filter=cover_date:%s&sort=cover_date:desc",
                BaseURL, PublicKey, dateRange
        );


        if (url == null) {
            throw new  BadUrlException();
        }

        // Create headers with User-Agent
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MyComicApp/1.0 (myemail@example.com)");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Send request with headers
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }



    @Scheduled(cron = "0 0 0 * * MON") //runs monday at midnight
    @CacheEvict(value ="thisWeeksComics", allEntries = true)
    public void clearWeeklyCache() {};


}
