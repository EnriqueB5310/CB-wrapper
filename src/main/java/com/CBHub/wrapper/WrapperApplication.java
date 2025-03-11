package com.CBHub.wrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrapperApplication.class, args);
	}

}
