package com.bonitasoft.reactiveworkshop.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Comment;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentsService {
	private EmitterProcessor<Comment> output;

	private RestTemplate commentsApiClient;
	private WebClient commentsStreamClient;

	@Autowired
	CommentsService(RestTemplate commentsApiClient, WebClient commentsStreamClient) {
		this.commentsStreamClient = commentsStreamClient;
		output = EmitterProcessor.create();
		this.commentsApiClient = commentsApiClient;
		consumeCommenntsStream(output).subscribe();
	}

	public List<Comment> getCommentsByArtisteId(String artistId) {
		return Arrays.asList(commentsApiClient.getForObject("/comments/{artistId}/last10", Comment[].class, artistId));
	}

	public Mono<Void> consumeCommenntsStream(EmitterProcessor<Comment> processor) {
		return commentsStreamClient.get().uri("/comments/stream").retrieve().bodyToFlux(Comment.class)
				.subscribeWith(processor).then();

	}

	public Flux<Comment> getComments(Set<String> artistsIds) {
		return this.output.filter(comment -> artistsIds.contains(comment.getArtisteId()));

	}

}
