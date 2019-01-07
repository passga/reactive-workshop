package com.bonitasoft.reactiveworkshop.service.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bonitasoft.reactiveworkshop.domain.Comment;

@Service
public class CommentsService {

	private RestTemplate commentsApiClient;

	@Autowired
	CommentsService (RestTemplate commentsApiClient){
		this.commentsApiClient = commentsApiClient;		
	}
	
	public List<Comment> getCommentsByArtisteId(String artistId) {
		return Arrays.asList(commentsApiClient.getForObject("/comments/{artistId}/last10", Comment[].class, artistId));
	}
	
}
