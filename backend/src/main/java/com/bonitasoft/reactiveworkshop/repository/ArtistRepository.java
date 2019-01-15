package com.bonitasoft.reactiveworkshop.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonitasoft.reactiveworkshop.domain.Artist;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {

	List<Artist> findByGenre(@Param("genre") String genre);
}
