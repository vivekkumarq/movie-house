package com.moviehouse.locationservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Location Service"))
public class LocationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LocationServiceApplication.class, args);
	}
}