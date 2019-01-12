package com.bonitasoft.reactiveworkshop.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.service.web.CommentsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenreApiTest {
	private static final String COMMENTS_SUFFIXE = "_Comments";

	private static final String NAME_SUFFIXE = "Name";


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
		Optional<List<Artist>> optional = Optional.of(getArtists(ids, genre));
		when(artistRepository.findByGenre(genre)).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		List<Comment> findCommentsByGenre = genreApi.findCommentsByGenre(genre);
		assertEquals(6, findCommentsByGenre.size());
		assertThat(findCommentsByGenre).extracting("artisteId").containsExactly("id1", "id1", "id2", "id2", "id3",
				"id3");
		assertThat(findCommentsByGenre).extracting("artistName").containsExactly("id1" + NAME_SUFFIXE,
				"id1" + NAME_SUFFIXE, "id2" + NAME_SUFFIXE, "id2" + NAME_SUFFIXE, "id3" + NAME_SUFFIXE,
				"id3" + NAME_SUFFIXE);

		assertThat(findCommentsByGenre).extracting("comment").containsExactly("userName1" + COMMENTS_SUFFIXE,
				"userName2" + COMMENTS_SUFFIXE, "userName1" + COMMENTS_SUFFIXE, "userName2" + COMMENTS_SUFFIXE,
				"userName1" + COMMENTS_SUFFIXE, "userName2" + COMMENTS_SUFFIXE);

		assertThat(findCommentsByGenre).extracting("userName").containsExactly("userName1", "userName2", "userName1",
				"userName2", "userName1", "userName2");
	}

	@Test(expected = NotFoundException.class)
	public void should_throw_not_found_exception_where_genre_not_exist() throws Exception {
		String genre = "toto";
		List<String> ids = Stream.of("id1", "id2", "id3").collect(Collectors.toList());
		List<String> userName = Stream.of("userName1", "userName2").collect(Collectors.toList());
		Optional<List<Artist>> optional = Optional.empty();
		when(artistRepository.findByGenre(genre)).thenReturn(optional);
		when(commentsRepository.getCommentsByArtisteId(anyString())).thenReturn(getComments(userName));

		genreApi.findCommentsByGenre(genre);

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
