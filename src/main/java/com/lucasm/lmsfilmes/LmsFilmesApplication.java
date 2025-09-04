package com.lucasm.lmsfilmes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LmsFilmesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsFilmesApplication.class, args);
	}

}
