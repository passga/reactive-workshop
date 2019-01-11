package com.bonitasoft.reactiveworkshop.service.web;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsStreamTest {
	@Autowired
	private CommentsService comments;

	RestTemplate commentsApiClient = new RestTemplateBuilder().rootUri("http://localhost:8080").build();
	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void should_return_8_artist_with() throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:8080");
		Flux<String> employeeMono = client.get().uri("/genre/Hard%20Rock/comments/stream").retrieve().bodyToFlux(String.class);

		employeeMono.subscribe(System.out::println);
		TimeUnit.SECONDS.sleep(50);

	}

}
