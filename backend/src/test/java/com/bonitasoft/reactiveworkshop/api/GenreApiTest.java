package com.bonitasoft.reactiveworkshop.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreApiTest {
	private static final String COMMENTS_SUFFIXE = "_Comments";

	private static final String NAME_SUFFIXE = "Name";

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private ArtistRepository artistRepository;

	@MockBean
	private CommentsService commentsRepository;

	@Autowired
	private GenreApi genreApi;

	@Test
	public void should_return_list_comment_when_artist_is_existing() throws Exception {
		String genre = "Hard%20Rock";
		List<String> ids = Stream.of("id1", "id2", "id3").collect(Collectors.toList());
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());

		String expected = "["//
				+ "{\"userName\":\"userName1\",\"comment\":\"userName1_Comments\",\"artistName\":\"id1Name\",\"artist\":\"id1\"}," //
				+ "{\"userName\":\"userName2\",\"comment\":\"userName2_Comments\",\"artistName\":\"id1Name\",\"artist\":\"id1\"},"//
				+ "{\"userName\":\"userName1\",\"comment\":\"userName1_Comments\",\"artistName\":\"id2Name\",\"artist\":\"id2\"}," //
				+ "{\"userName\":\"userName2\",\"comment\":\"userName2_Comments\",\"artistName\":\"id2Name\",\"artist\":\"id2\"}," //
				+ "{\"userName\":\"userName1\",\"comment\":\"userName1_Comments\",\"artistName\":\"id3Name\",\"artist\":\"id3\"},"//
				+ "{\"userName\":\"userName2\",\"comment\":\"userName2_Comments\",\"artistName\":\"id3Name\",\"artist\":\"id3\"}]";
		Optional<List<Artist>> optional = Optional.of(getArtists(ids, genre));
		when(artistRepository.findByGenre(genre)).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		assertThat(this.restTemplate.getForObject("/genres/" + genre + "/comments", String.class)).isEqualTo(expected);
	}

	@Test
	public void should_throw_not_found_exception_where_genre_not_exist() throws Exception {
		String genre = "toto";
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());
		Optional<List<Artist>> optional = Optional.empty();
		when(artistRepository.findByGenre(genre)).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange("/genres/" + genre + "/comments", HttpMethod.GET,
				entity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	private List<Comment> getComments(List<String> users) {
		return users.stream().map(userName -> {
			return Comment.builder().userName(userName).comment(userName + COMMENTS_SUFFIXE).build();
		}).collect(Collectors.toList());

	}

	private List<Artist> getArtists(List<String> ids, String genre) {
		return ids.stream().map(id -> {
			return Artist.builder().id(id).name(id + NAME_SUFFIXE).genre(genre).build();
		}).collect(Collectors.toList());

	}

}
