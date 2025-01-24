package com.CBHub.wrapper.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



//Handles eviction of caches
@Component
public class cacheUtil {
    @Autowired
    CacheManager cacheManager;

    //Clears the cache for weekly comics every monday at 12:00AM;
    @Scheduled(cron = "0 0 0 * * MON") //runs monday at midnight
    @CacheEvict(value ="thisWeeksComics", allEntries = true)
    public void clearWeeklyCache() {};

}
