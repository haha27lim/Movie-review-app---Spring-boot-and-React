package com.example.server.services;

import java.io.IOException;
import java.io.StringReader;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.server.models.Comment;
import com.example.server.models.Review;
import com.example.server.repositories.MovieRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


@Service
public class MovieService {

	@Value("${moviereview_Url}")
	private String BASE_URL;

	@Value("${apikey}")
	private String REVIEW_API_KEY;

	@Value("${apihost}")
	private String REVIEW_API_HOST;

	@Autowired
    private MovieRepository movieRepo;

	// TODO: Task 4
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	public Review searchReviews(String movieTitle, Boolean includeReviews) throws IOException {

		String url = UriComponentsBuilder
						.fromUriString(BASE_URL)
						.path(movieTitle.replace(" ", "-").toLowerCase())
						.queryParam("reviews", "true")
						.toUriString();

		//Set the headers you need send
		final HttpHeaders headers = new HttpHeaders();
		String reviewApiKey = REVIEW_API_KEY;
		String reviewApiHost = REVIEW_API_HOST;

		headers.set("X-RapidAPI-Key", reviewApiKey);
		headers.set("X-RapidAPI-Host", reviewApiHost);

		// Build the request entity
        HttpEntity<Void> entity = new HttpEntity<>(headers);

		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = null;

        try {
            // Send the request to the movie reviews API and get the response entity
            resp = template.exchange(url,  HttpMethod.GET, entity, String.class);
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }

		 // Get the response payload as a string
		String payload = resp.getBody();
		System.out.println("Payload: " + payload);

		// Parse the JSON payload using the Java EE JSONReader API
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject reviewResp = reader.readObject();
		Review review = Review.toReview(reviewResp);

		return review;
	}

	public Comment insertComment(Comment r){
        return movieRepo.insertComment(r);
    }
}
