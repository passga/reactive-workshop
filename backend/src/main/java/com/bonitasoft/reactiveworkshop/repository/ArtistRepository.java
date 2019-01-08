package com.bonitasoft.reactiveworkshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonitasoft.reactiveworkshop.domain.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {// extends MongoRepository<Artist, String> {

	@Query("SELECT art FROM Artist art  WHERE art.genre=(:genre)")
	Optional<List<Artist>> findByGenre(@Param("genre") String genre);
}
