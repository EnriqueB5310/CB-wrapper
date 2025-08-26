package com.CBHub.wrapper;


import com.CBHub.wrapper.serviceImpl.WeeklyComics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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


    @Test
    void testIfCacheWorks() {
    //First call to populate cache
        Map<String,Object> firstCall = spyWeeklyComics.getWeeklyComics();
    //should come from cache
        Map<String, Object> secondCall = spyWeeklyComics.getWeeklyComics();

        assertEquals(firstCall,secondCall);

        verify(spyWeeklyComics, times(1)).getWeeklyComics();

    }

    @Test
    void testCacheEviction() {

        spyWeeklyComics.getWeeklyComics();

        //evict manually
        spyWeeklyComics.clearWeeklyCache();

        //call again (method should run again since no cache)
        spyWeeklyComics.getWeeklyComics();

        verify(spyWeeklyComics,times(2)).getWeeklyComics();

    }

}
