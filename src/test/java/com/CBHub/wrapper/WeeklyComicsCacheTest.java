package com.CBHub.wrapper;


import com.CBHub.wrapper.serviceImpl.WeeklyComics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//ensures cache resets between tests
public class WeeklyComicsCacheTest {

    @Autowired
    private WeeklyComics weeklyComics;

    @Autowired
    private CacheManager cacheManager;

    @MockitoSpyBean
    private WeeklyComics spyWeeklyComics;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void testIfCacheWorks() {

        // Arrange: mock API response
        Map<String, Object> mockComic = Map.of(
                "name", "Dummy Comic",
                "cover_date", "2025-11-10",
                "image", Map.of("original_url", "https://example.com/dummy.jpg")
        );

        Map<String, Object> dummyResponse = Map.of(
                "results", List.of(mockComic)
        );

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(dummyResponse));

        // First call to populate cache
        List<Map<String, Object>> firstCall = spyWeeklyComics.getWeeklyComics();

        // Should come from cache
        List<Map<String, Object>> secondCall = spyWeeklyComics.getWeeklyComics();

        // Assert that both calls return the same result
        assertEquals(firstCall, secondCall);

        // Verify API was called only once
        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));

        // Verify method was called twice but API only once due to caching
        verify(spyWeeklyComics, times(1)).getWeeklyComics();
    }

    @Test
    void testCacheEviction() {

        Map<String, Object> dummyResponse = Map.of("title", "Evicted Comic");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(dummyResponse));

        spyWeeklyComics.getWeeklyComics();

        //evict manually
        spyWeeklyComics.clearWeeklyCache();

        //call again (method should run again since no cache)
        spyWeeklyComics.getWeeklyComics();

        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));

        verify(spyWeeklyComics,times(2)).getWeeklyComics();

    }

}
