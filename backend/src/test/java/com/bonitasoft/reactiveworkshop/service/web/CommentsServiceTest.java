package com.bonitasoft.reactiveworkshop.service.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsServiceTest {

	@Autowired
	RestTemplate commentsApiClient;

	@Autowired
	CommentsService service;

	private MockRestServiceServer mockServer;

	@Before
	public void setUp() {
		RestGatewaySupport gateway = new RestGatewaySupport();
		gateway.setRestTemplate(commentsApiClient);
		mockServer = MockRestServiceServer.createServer(gateway);
	}

	@Test
	public void testGetRootResourceOnce() throws JsonParseException, JsonMappingException, IOException {
		String body = "["//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"nolanb\",\"comment\":\"Purus, vel ultricies sit Nam eget amet purus.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"jwolf\",\"comment\":\"Lectus aliquet congue scelerisque augue. Amet metus adipisicing pede.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"gavinb\",\"comment\":\"Metus aliquam Phasellus sodales massa lectus ornare, aliquet, lacinia.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"eriley\",\"comment\":\"Scelerisque congue orci. At metus Praesent congue interdum, nisi.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"hunterc\",\"comment\":\"Nibh neque. Urna.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"astafford\",\"comment\":\"Est. Praesent Phasellus vitae metus.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"kennedys\",\"comment\":\"Pede neque. Eget.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"leahl\",\"comment\":\"Suscipit Etiam dui Phasellus.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"clairec\",\"comment\":\"Sit consectetuer nisi. Metus elit congue.\"},"//
				+ "{\"artist\":\"6125d26e6d4d0d9b1a29b298700d8fe1\",\"userName\":\"zoeyc\",\"comment\":\"Est. Et urna est metus metus Etiam dui erat.\"}"//
				+ "]";
	
		mockServer.expect(once(), requestTo("http://localhost:3004/comments/6125d26e6d4d0d9b1a29b298700d8fe1/last10"))
				.andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

		
		List<Comment> expected = generateListComments(body);
		List<Comment> result = service.getCommentsByArtisteId("6125d26e6d4d0d9b1a29b298700d8fe1");
		assertEquals(10, result.size());

		assertThat(result).isEqualTo(expected);

	}

	private List<Comment> generateListComments(String body) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return Arrays.asList(objectMapper.readValue(body, Comment[].class));
		
	}

}
