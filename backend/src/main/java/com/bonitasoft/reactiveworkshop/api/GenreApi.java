package com.bonitasoft.reactiveworkshop.api;

import java.util.List;
import java.util.Map;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class GenreApi {

	private ArtistRepository artistRepository;
	private CommentsService commentsRepository;

	@Autowired
	public GenreApi(ArtistRepository artistRepository, CommentsService commentsRepository, ObjectMapper jsonMapper) {
		this.artistRepository = artistRepository;
		this.commentsRepository = commentsRepository;
	}

	@GetMapping("/genres")
	public List<String> findAll() {
		return artistRepository.findAll().stream().map(Artist::getGenre).filter(g -> !g.isEmpty()).distinct().sorted()
				.collect(Collectors.toList());
	}

	@GetMapping("/genres/{genre}/comments")
	public List<Comment> findCommentsByGenre(@PathVariable String genre) throws NotFoundException {
		List<Comment> collect = artistRepository.findByGenre(genre)
				.orElseThrow(() -> new IllegalArgumentException("no artist found for gene " + genre)).stream()
				.map(artist -> {
					List<Comment> commentsByArtisteId = commentsRepository.getCommentsByArtisteId(artist.getId());
					return commentsByArtisteId.stream().map(comment -> {
						return new Comment(comment.getUserName(), comment.getComment(), artist.getId(),
								artist.getName());
					}).collect(Collectors.toList());
				}).flatMap(List::stream).collect(Collectors.toList());
		return collect;
	}

	@GetMapping(path = "/genre/{genre}/comments/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Comment> getCommentsStreamByGenre(@PathVariable String genre) {
		Flux<Map<String, String>> artistNameById = Flux.just(artistRepository.findByGenre(genre).get().stream()
				.collect(Collectors.toMap(Artist::getId, Artist::getName)));
		return artistNameById.flatMap(map -> {
			log.debug("get comments for {} ", map.keySet());
			return commentsRepository.getComments(map.keySet()).log().map(comment -> {
				log.debug("found comments {}", comment);
				comment.setArtistName(map.get(comment.getArtisteId()));

				return comment;
			});
		}).log().doOnError(System.out::println);
	}

}
