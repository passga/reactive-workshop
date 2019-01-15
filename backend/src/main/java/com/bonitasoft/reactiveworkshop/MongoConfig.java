package com.bonitasoft.reactiveworkshop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ArtistRepository.class)
public class MongoConfig  {

	
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	@Bean
	public MongoClient mongoClient() {
	    return MongoClients.create(mongoUri);
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
	    return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory());
	}

	@Bean
	public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
	    return new SimpleReactiveMongoDatabaseFactory(mongoClient(), "mongotest");
	}
}
