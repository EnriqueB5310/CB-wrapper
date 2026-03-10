package com.CBHub.wrapper.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.LogRecord;

@Component
public class RateLimitFilter implements Filter {

 private  static final int MaxRequests = 10;
 private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;


        String clientIp = httpRequest.getRemoteAddr();
        requestCounts.putIfAbsent(clientIp, new AtomicInteger());
        int currentCount = requestCounts.get(clientIp).incrementAndGet();


        if (currentCount > MaxRequests) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpServletResponse.getWriter().write("Too many requests, Try later");
        }
        filterChain.doFilter(servletRequest,servletResponse);

    }
}
