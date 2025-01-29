package com.am.SalesConversionAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main class to run the Spring Boot application
 */
@SpringBootApplication
@EnableCaching
public class SalesConversionApiApplication {

	/**
	 * Main method to run the Spring Boot application
	 * @param args Arguments to the main method.
	 */
    public static void main(String[] args) {
		SpringApplication.run(SalesConversionApiApplication.class, args);
	}
}
