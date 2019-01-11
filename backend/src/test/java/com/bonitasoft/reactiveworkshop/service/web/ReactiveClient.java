package com.bonitasoft.reactiveworkshop.service.web;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@SpringBootApplication

public class ReactiveClient {
	ObjectMapper mapper = new ObjectMapper();

	@Bean

	WebClient client() {

		return WebClient.create("http://localhost:8080");

	}

	@Bean

	CommandLineRunner demo(WebClient client) {

		return args -> {

			client.get()

					.uri("/genre/Rock/comments/stream")

					.accept(MediaType.APPLICATION_STREAM_JSON)

					.exchange()

					.flatMapMany(cr -> cr.bodyToFlux(Comment.class)).flatMap(comment -> write(comment))

					.subscribe(System.out::println);

		};

	}

	public Mono<String> write(Comment comment) {
		try {

			return Mono.just(mapper.writeValueAsString(comment));
		} catch (JsonProcessingException e) {
			return Mono.error(e);
		}
	}

	public static void main(String[] args) {

		new SpringApplicationBuilder(ReactiveClient.class)

				.properties(Collections.singletonMap("server.port", "8081"))

				.run(args);

	}

}