package com.bonitasoft.reactiveworkshop.api;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class GenreApi{
	ObjectMapper mapper = new ObjectMapper();
	private ArtistRepository artistRepository;
	private CommentsService commentsRepository;

	@Autowired
	public GenreApi(ArtistRepository artistRepository, CommentsService commentsRepository) {
		this.artistRepository = artistRepository;
		this.commentsRepository = commentsRepository;
	}

	@GetMapping("/genres")
	public Flux<String> findAll() {
		return artistRepository.findAll().map(Artist::getGenre).filter(g -> !g.isEmpty()).distinct().sort();
	}

	@GetMapping("/genres/{genre}/comments")
	public Flux<Comment> findCommentsByGenre(@PathVariable String genre) throws NotFoundException {
		return artistRepository.findByGenre(genre).flatMap(artist -> {
			log.info("artist {}", artist);
			return commentsRepository.getCommentsByArtisteId(artist.getId()).log().map(comment -> {
				log.info("comment {}", comment);
				Comment commentA= new Comment(comment.getUserName(), comment.getComment(), artist.getId(), artist.getName());
				log.info("comment after {}", comment);
				return commentA;
			});
		});
	}




	@GetMapping(path = "/genre/{genre}/comments/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Comment> handle(@PathVariable String genre) {
		return commentsRepository.getComments().flatMap(comment2 -> {
			return get(genre, comment2).map(comment -> {
				comment2.setArtistName(comment.getArtistName());
				return comment2;
			});

		});

	}

	private Mono<Comment> get(String genre, Comment comment) {
		return artistRepository.findById(comment.getArtisteId()).filter(artist -> artist.getGenre().equals(genre))
				.map(artist -> Comment.builder().artistName(artist.getName()).build());

	}

	public Mono<String> write(Comment comment) {
		try {

			return Mono.just(mapper.writeValueAsString(comment));
		} catch (JsonProcessingException e) {
			return Mono.error(e);
		}
	}

}
