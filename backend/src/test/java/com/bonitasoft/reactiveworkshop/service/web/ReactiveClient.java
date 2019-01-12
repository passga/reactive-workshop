package com.bonitasoft.reactiveworkshop.service.web;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveClient {
	ObjectMapper mapper = new ObjectMapper();

	@Test

	public void client() throws InterruptedException {

		 WebClient.create("http://localhost:8080").get()

				.uri("/genre/Rock/comments/stream")

				.accept(MediaType.APPLICATION_STREAM_JSON)

				.exchange()

				.flatMapMany(cr -> cr.bodyToFlux(Comment.class)).flatMap(comment -> write(comment))

				.subscribe(System.out::println);

		 TimeUnit.SECONDS.sleep(50);
	}

	public Mono<String> write(Comment comment) {
		try {

			return Mono.just(mapper.writeValueAsString(comment));
		} catch (JsonProcessingException e) {
			return Mono.error(e);
		}
	}

}