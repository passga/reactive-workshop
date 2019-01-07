package com.bonitasoft.reactiveworkshop.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bonitasoft.reactiveworkshop.domain.Artist;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArtistRepositoryTest  {
	@Autowired
	private ArtistRepository artistRepository;
	

	


		

		@Test
		public void should_return_list() {

			
			List<Artist> contacts = artistRepository.findByGenre("Hard Rock");
			
			assertEquals(1, contacts.size());
			
		}

		
	
	
}
