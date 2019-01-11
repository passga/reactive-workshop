package com.bonitasoft.reactiveworkshop.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonitasoft.reactiveworkshop.domain.Artist;

import reactor.core.publisher.Flux;

@Repository
public interface ArtistRepository extends ReactiveMongoRepository<Artist, String> {// extends MongoRepository<Artist,
																					// String> {

	Flux<Artist> findByGenre(@Param("genre") String genre);
}
