package com.bonitasoft.reactiveworkshop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ArtistAPI {

	private ArtistRepository artistRepository;
	private CommentsService commentsRepository;

	public ArtistAPI(ArtistRepository artistRepository, CommentsService commentsRepository) {
		this.artistRepository = artistRepository;
		this.commentsRepository = commentsRepository;
	}

	@GetMapping("/artist/{artistId}")
	public Mono<Artist> findById(@PathVariable String artistId) throws NotFoundException {
		return artistRepository.findById(artistId);
	}

	@GetMapping("/artists")
	public Flux<Artist> findAll() throws NotFoundException {
		return artistRepository.findAll();
	}

	@GetMapping("/artist/{artistId}/comments")
	public Mono<Artist> findLast10CommentsByArtistId(@PathVariable String artistId) throws NotFoundException {
		return artistRepository.findById(artistId).map(artist -> {
			commentsRepository.getCommentsByArtisteId(artist.getId()).doOnNext(comment -> {
				artist.addComment(comment);				
			});
			return artist;
		});
	}

	private void addComment(Artist artist) {
		

	}
}
