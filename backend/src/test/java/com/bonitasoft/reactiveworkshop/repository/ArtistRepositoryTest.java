package com.bonitasoft.reactiveworkshop.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bonitasoft.reactiveworkshop.domain.Artist;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtistRepositoryTest {
	@Autowired
	private ArtistRepository artistRepository;

	@Test
	public void should_return_8_artist_with() {
		List<Artist> artists = artistRepository.findByGenre("Hard Rock");
		assertEquals(false, artists.isEmpty());
		assertEquals(8, artists.size());
	}

	@Test
	public void should_return_if_present_false_when_genre_is_unknown() {
		List<Artist> artists = artistRepository.findByGenre("testTes");
		assertEquals(true, artists.isEmpty());
	}

}
