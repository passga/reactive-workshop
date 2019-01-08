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
		Optional<List<Artist>> artists = artistRepository.findByGenre("Hard Rock");
		assertEquals(true, artists.isPresent());
		assertEquals(8, artists.get().size());
	}

	@Test
	public void should_return_if_presnt_false_when_gener_is_unknown() {
		Optional<List<Artist>> artists = artistRepository.findByGenre("toto");
		assertEquals(false, artists.isPresent());
	}

}
