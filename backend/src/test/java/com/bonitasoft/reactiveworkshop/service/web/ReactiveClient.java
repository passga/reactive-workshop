package com.bonitasoft.reactiveworkshop.service.web;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveClient {

	@Test

	public void client() throws InterruptedException {
		WebClient.create("http://localhost:8080").get().uri("/genre/Rock/comments/stream")
				.accept(MediaType.APPLICATION_STREAM_JSON).retrieve().bodyToFlux(Comment.class)
				.subscribe(System.out::println);

		TimeUnit.SECONDS.sleep(50);
	}

}