package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;



import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "artistId", "artistName", "genre", "comments" })
public class Artist {

	@Id
	@JsonProperty("artistId")
	private String id;
	@JsonProperty("artistName")
	private String name;
	
	private String genre;

	@Transient
	private List<Comment> comments;
}
