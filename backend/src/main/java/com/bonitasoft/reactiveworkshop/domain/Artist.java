package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;

//@Document
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
