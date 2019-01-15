package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;

//@Document
@EntityScan
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Artist {

	@Id
	@JsonProperty("artistId")
	private String id;
	@JsonProperty("artistName")
	private String name;
	private String genre;

	@org.springframework.data.annotation.Transient
	private List<Comment> comments;

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
