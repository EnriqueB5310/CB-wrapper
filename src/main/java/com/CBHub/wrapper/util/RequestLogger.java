package com.CBHub.wrapper.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;



//utility logger to log req/response speeds
@Component
@WebFilter("/**")
public class RequestLogger implements Filter {


    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Instant start = Instant.now();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            Instant finish = Instant.now();
            long time = Duration.between(start,finish).toMillis();
            logger.trace("{}: {} ms ", ((HttpServletRequest) servletRequest).getRequestURI(), time);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}