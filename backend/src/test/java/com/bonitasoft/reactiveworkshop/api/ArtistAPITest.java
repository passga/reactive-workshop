package com.bonitasoft.reactiveworkshop.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
public class ArtistAPITest {

	private static final String COMMENTS_SUFFIXE = "_Comments";

	private static final String NAME_SUFFIXE = "Name";

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private ArtistRepository artistRepository;

	@MockBean
	private CommentsService commentsRepository;

	@Test
	public void should_return_expected_artist() throws Exception {
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());
		Optional<Artist> optional = Optional.of(new Artist("id1", "artistName", "Hard Rock", null));
		String expected = "{\"artistId\":\"id1\",\"artistName\":\"artistName\",\"genre\":\"Hard Rock\"}";

		when(artistRepository.findById("id1")).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		assertThat(this.restTemplate.getForObject("/artist/id1", String.class)).isEqualTo(expected);
	}

	@Test
	public void should_throw_not_found_exception_where_artist_not_exist() throws Exception {
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());
		Optional<Artist> optional = Optional.empty();
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));
		when(artistRepository.findById("id1")).thenReturn(optional);

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange("/artist/rammstein", HttpMethod.GET, entity,
				String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	public void should_return_artists_with_comments() throws Exception {
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());
		Optional<Artist> optional = Optional.empty();
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));
		when(artistRepository.findById("id1")).thenReturn(optional);

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange("/artist/id3", HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	public void should_return_not_found_when_getçartists_with_comments() throws Exception {
		Optional<Artist> optional = Optional.empty();
		when(artistRepository.findById("id1")).thenReturn(optional);

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange("/artist/id1/comments", HttpMethod.GET, entity,
				String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		verify(commentsRepository, never()).getCommentsByArtisteId(anyString());

	}

	@Test
	public void should_return_artist_and_comments() throws Exception {
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());

		String expected = "{\"artistId\":\"id1\",\"artistName\":\"artistName\",\"genre\":\"Hard Rock\","//
				+ "\"comments\":"//
				+ "[{\"userName\":\"userName1\",\"comment\":\"userName1_Comments\"}," //
				+ "{\"userName\":\"userName2\",\"comment\":\"userName2_Comments\"}"//
				+ "]" //
				+ "}";

		Optional<Artist> optional = Optional.of(new Artist("id1", "artistName", "Hard Rock", null));
		when(artistRepository.findById("id1")).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId("id1")).thenReturn(getComments(userName));

		assertThat(this.restTemplate.getForObject("/artist/id1/comments", String.class)).isEqualTo(expected);
	}

	@Test
	public void should_return_all_artist() throws Exception {
		String genre = "Hard Rock";
		List<String> ids = Stream.of("id1", "id2").collect(Collectors.toList());
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());

		String expected = "[{\"artistId\":\"id1\",\"artistName\":\"id1Name\",\"genre\":\"Hard Rock\"},"//
				+ "{\"artistId\":\"id2\",\"artistName\":\"id2Name\",\"genre\":\"Hard Rock\"}]";

		when(artistRepository.findAll()).thenReturn(getArtists(ids, genre));
		when(commentsRepository.getCommentsByArtisteId("id1")).thenReturn(getComments(userName));

		assertThat(this.restTemplate.getForObject("/artists", String.class)).isEqualTo(expected);
	}

	@Test
	public void should_return_empty_list_when_zero_artists_store() throws Exception {
		when(artistRepository.findAll()).thenReturn(new ArrayList<>());
		assertThat(this.restTemplate.getForObject("/artists", String.class)).isEqualTo("[]");
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
