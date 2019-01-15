package com.bonitasoft.reactiveworkshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;

@SpringBootApplication
public class ReactiveWorkshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWorkshopApplication.class, args);
	}

	@Bean
	RestTemplate commentsApiClient() {
		return new RestTemplateBuilder().rootUri("http://localhost:3004").build();
	}
	
	

}
