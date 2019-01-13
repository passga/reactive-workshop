package com.bonitasoft.reactiveworkshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ReactiveWorkshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWorkshopApplication.class, args);
	}

	@Bean
	RestTemplate commentsApiClient() {
		return new RestTemplateBuilder().rootUri("http://localhost:3004").build();
	}

	@Bean
	WebClient commentsStreamClient() {
		return WebClient.create("http://localhost:3004");
	}

	@Bean
	ObjectMapper jsonMapper() {
		return new ObjectMapper();
	}

}
