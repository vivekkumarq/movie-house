package com.moviehouse.ticketservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Ticket Service"))
@EnableScheduling
public class TicketServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TicketServiceApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public ModelMapper getModelMapper(){
		return new ModelMapper();
	}
}
