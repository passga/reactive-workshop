package com.bonitasoft.reactiveworkshop.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreApiTest {
	private static final String COMMENTS_SUFFIXE = "_Comments";

	private static final String NAME_SUFFIXE = "Name";

	@MockBean
	private ArtistRepository artistRepository;

	@MockBean
	private CommentsService commentsRepository;

	@Autowired
	private GenreApi genreApi;

	@Autowired
	private WebTestClient webClient;

	@Test
	public void should_return_list_comment_when_artist_is_existing() throws Exception {
		String genre = "Hard%20Rock";
		Flux<String> ids = Flux.just("id1", "id2", "id3");
		Flux<String> userName = Flux.just("userName1", "userName2");
		Flux<Artist> artist = getArtists(ids, genre);

		AtomicInteger adder = new AtomicInteger();
		when(artistRepository.findByGenre(genre)).thenReturn(artist);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		webClient.get().uri("/genres/" + genre + "/comments").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectBodyList(Comment.class).hasSize(6)
				.contains(new Comment("userName1", "userName1" + COMMENTS_SUFFIXE, "id1", "id1" + NAME_SUFFIXE),
						new Comment("userName2", "userName2" + COMMENTS_SUFFIXE, "id1", "id1" + NAME_SUFFIXE),
						new Comment("userName1", "userName1" + COMMENTS_SUFFIXE, "id2", "id2" + NAME_SUFFIXE),
						new Comment("userName2", "userName2" + COMMENTS_SUFFIXE, "id2", "id2" + NAME_SUFFIXE),
						new Comment("userName1", "userName1" + COMMENTS_SUFFIXE, "id3", "id3" + NAME_SUFFIXE),
						new Comment("userName2", "userName2" + COMMENTS_SUFFIXE, "id3", "id3" + NAME_SUFFIXE));

	}

	@Test
	public void should_throw_not_found_exception_where_genre_not_exist() throws Exception {
		String genre = "toto";
		Flux<String> ids = Flux.just("id1", "id2", "id3");
		Flux<String> userName = Flux.just("userName1", "userName2");
		Flux<Artist> optional = Flux.empty();
		when(artistRepository.findByGenre(genre)).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		webClient.get().uri("/genres/" + genre + "/comments").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();

	}

	private Flux<Comment> getComments(Flux<String> users) {
		return users.map(userName -> {
			return Comment.builder().userName(userName).comment(userName + COMMENTS_SUFFIXE).build();
		});

	}

	private Flux<Artist> getArtists(Flux<String> ids, String genre) {
		return ids.map(id -> {
			return Artist.builder().id(id).name(id + NAME_SUFFIXE).genre(genre).build();
		});

	}

}
