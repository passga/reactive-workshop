package com.bonitasoft.reactiveworkshop.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CommentsService extends Observable {
	private EmitterProcessor<Comment> output;
	// private RestTemplate commentsApiClient;
	WebClient client = WebClient.create("http://localhost:3004");

	@Autowired
	CommentsService() {
		output = EmitterProcessor.create();
		consumeCommenntsStream(output).subscribe();
	}

	@PostConstruct
	public void subscribeToComments() {

	}

	public Flux<Comment> getCommentsByArtisteId(String artistId) {
		return client.get().uri("/comments/{artistId}/last10").retrieve().bodyToFlux(Comment.class).log();
	}

	public Mono<Void> consumeCommenntsStream(EmitterProcessor<Comment> processor) {
		return client.get().uri("/comments/stream").retrieve().bodyToFlux(Comment.class).subscribeWith(processor)
				.then();
	}

	public EmitterProcessor<Comment> getComments() {
		log.info("number subscriber {}, pending {}", output.downstreamCount(), output.getPending());
		return this.output;

	}

}
