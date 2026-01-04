package com.CBHub.wrapper.serviceImpl;




import com.CBHub.wrapper.exceptions.BadUrlException;
import com.CBHub.wrapper.services.WeeklyComicService;
import com.CBHub.wrapper.util.md5Hasher;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class WeeklyComics implements WeeklyComicService {

    private final CacheManager cacheManager;
    private final md5Hasher hasher;
    private final RestTemplate restTemplate;

    private static final String BaseURL = "https://comicvine.gamespot.com/api/issues/";
    private static final String PublicKey = "2556a3bdb79e127ee06421ae41720c3a17a9bca7";

    public WeeklyComics(md5Hasher hasher, CacheManager cacheManager, RestTemplate restTemplate) {
        this.cacheManager = cacheManager;
        this.hasher = hasher;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "thisWeeksComics")
    public List<Map<String, Object>> getWeeklyComics() {

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateRange = weekStart.format(formatter) + "|" + weekEnd.format(formatter);

        String url = String.format(
                "%s?api_key=%s&format=json&filter=cover_date:%s,volume.publisher.name:DC Comics|Marvel,volume.first_issue.language:en&sort=cover_date:desc&limit=100",
                BaseURL, PublicKey, dateRange
        );

        if (url == null) {
            throw new BadUrlException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MyComicApp/1.0 (myemail@example.com)");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("results")) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
        List<Map<String, Object>> trimmed = new ArrayList<>();

        for (Map<String, Object> comic : results) {

            // Get the comic's ID
            Object comicId = comic.get("id");

            String releaseDate = (String) comic.get("cover_date");

            Map<String, Object> imageData = (Map<String, Object>) comic.get("image");
            String coverImage = imageData != null ? (String) imageData.get("original_url") : null;

            Map<String, Object> volume = (Map<String, Object>) comic.get("volume");
            String volumeName = volume != null ? (String) volume.get("name") : "Unknown Series";

            String issueNumber = (String) comic.get("issue_number");
            String subName = (String) comic.get("name");

            String title;
            if (subName != null && !subName.trim().isEmpty()) {
                title = volumeName + " #" + issueNumber + " – " + subName;
            } else {
                title = volumeName + " #" + issueNumber;
            }

            Map<String, Object> trimmedComic = new HashMap<>();
            trimmedComic.put("id", comicId);  // Add the ID here
            trimmedComic.put("title", title);
            trimmedComic.put("releaseDate", releaseDate);
            trimmedComic.put("coverImage", coverImage);

            trimmed.add(trimmedComic);
        }

        return trimmed;
    }

    @Scheduled(cron = "0 0 0 * * MON")
    @CacheEvict(value = "thisWeeksComics", allEntries = true)
    public void clearWeeklyCache() {}

}
