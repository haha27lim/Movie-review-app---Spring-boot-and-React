package com.example.server.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.models.Comment;
import com.example.server.models.Review;
import com.example.server.repositories.MovieRepository;
import com.example.server.services.MovieService;
import jakarta.json.JsonObject;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

	// TODO: Task 3, Task 4, Task 8

	@Autowired
    private MovieService movieSvc;

	@Autowired
	private MovieRepository movieRepo;

	private Logger logger = LoggerFactory.getLogger(MovieController.class);

	@GetMapping(path = "/search/{title}")
	public ResponseEntity<String> getSearch(@PathVariable String title) throws IOException {

		
		Review review = movieSvc.searchReviews(title, true);

		// Check if the review is null before proceeding
		if (review == null) {
			String message = String.format("Review on %s not found", title);
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body("{ \"message\": \"" + message + "\" }");
		}

		if (review != null) {
			review.setCommentCount(movieRepo.countComments(title));
		}

		// use the toJson method of the Review class
		JsonObject result = review.toJson();

		
		return ResponseEntity
		.status(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON)
		.body(result.toString());
		
	}
	

	@PostMapping(path = "/comment", consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> saveComment(@RequestParam String title, @RequestParam String name, 
			@RequestParam Integer rating, @RequestParam String comment) {
		Comment c= new Comment();
		c.setTitle(title);
		c.setName(name);
		c.setRating(rating);
		c.setComment(comment);
		Comment r = movieSvc.insertComment(c);
		
		return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(r.toJSON().toString());
	}
}
