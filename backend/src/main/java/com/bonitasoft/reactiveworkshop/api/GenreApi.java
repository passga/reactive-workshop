package com.bonitasoft.reactiveworkshop.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;

@RestController
public class GenreApi {

	private ArtistRepository artistRepository;
	private CommentsService commentsRepository;

	@Autowired
	public GenreApi(ArtistRepository artistRepository, CommentsService commentsRepository) {
		this.artistRepository = artistRepository;
		this.commentsRepository = commentsRepository;
	}

	@GetMapping("/genres")
	public List<String> findAll() {
		return artistRepository.findAll().stream().map(Artist::getGenre).filter(g -> !g.isEmpty()).distinct().sorted()
				.collect(Collectors.toList());
	}

	@GetMapping("/genres/{genre}/comments")
	public List<Comment> findCommentsByGenre(@PathVariable String genre) {
		return artistRepository.findByGenre(genre).stream().map(artist -> {
					return commentsRepository.getCommentsByArtisteId(artist.getId()).stream().map(comment -> {
						comment.setArtisteId(artist.getId());
						comment.setArtistName(artist.getName());
						return comment;
					}).collect(Collectors.toList());
				}).flatMap(List::stream).collect(Collectors.toList());
	}

}
