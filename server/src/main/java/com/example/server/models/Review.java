package com.example.server.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

// DO NOT MODIFY THIS CLASS
public class Review {
    private String title;
    private String director;
    private String description;
    private String genre;
    private String thumbnailUrl;
    private Date releaseDate;
    private int metaScore;
    private int userScore;
    private List<UserReview> recentUserReviews;

	private int commentCount;

	// Getters and setters for main class
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDirector() { return director; }
	public void setDirector(String director) { this.director = director; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getGenre() { return genre; }
	public void setGenre(String genre) { this.genre = genre; }

	public String getThumbnailUrl() { return thumbnailUrl; }
	public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

	public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public int getMetaScore() { return metaScore; }
    public void setMetaScore(int metaScore) { this.metaScore = metaScore; }

    public int getUserScore() { return userScore; }
    public void setUserScore(int userScore) { this.userScore = userScore; }

    public List<UserReview> getRecentUserReviews() { return recentUserReviews; }
    public void setRecentUserReviews(List<UserReview> recentUserReviews) { this.recentUserReviews = recentUserReviews; }

	public int getCommentCount() { return this.commentCount; }
	public void setCommentCount(int commentCount) { this.commentCount = commentCount; };
	


	// Nested class for user reviews
    public static class UserReview {
        private String name;
        private Date date;
        private int grade;
        private String body;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
        
        public int getGrade() { return grade; }
        public void setGrade(int grade) { this.grade = grade; }
        
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }

		public UserReview(String name, Date date, int grade, String body) {
			this.name = name;
			this.date = date;
			this.grade = grade;
			this.body = body;
		}
    }

    public JsonObject toJson() {
        JsonArrayBuilder reviewsBuilder = Json.createArrayBuilder();
        for (UserReview review : getRecentUserReviews()) {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("name", review.getName())
                .add("grade", review.getGrade())
                .add("body", review.getBody());

				if (review.getDate() != null) {
					builder.add("date", review.getDate().toString());
				} else {
					builder.addNull("date");
				}
			reviewsBuilder.add(builder.build());
        }

        return Json.createObjectBuilder()
            .add("title", getTitle() != null ? getTitle() : "")
            .add("director", getDirector() != null ? getDirector() : "")
            .add("description", getDescription() != null ? getDescription() : "")
            .add("genre", getGenre() != null ? getGenre() : "")
            .add("thumbnailUrl", getThumbnailUrl() != null ? getThumbnailUrl() : "")
            .add("releaseDate", getReleaseDate() != null ? getReleaseDate().toString() : "")
            .add("metaScore", getMetaScore())
            .add("userScore", getUserScore())
            .add("recentUserReviews", reviewsBuilder.build())
			.add("commentCount", getCommentCount())
            .build();
    }

    public static Review toReview(JsonObject json) {
        Review review = new Review();
		// Safely retrieve strings, handling null values gracefully
        review.setTitle(json.containsKey("title") && !json.isNull("title")
                ? json.getString("title") : "");
        review.setDirector(json.containsKey("director") && !json.isNull("director")
                ? json.getString("director") : "");
        review.setDescription(json.containsKey("description") && !json.isNull("description")
                ? json.getString("description") : "");
        review.setGenre(json.containsKey("genre") && !json.isNull("genre")
                ? json.getString("genre") : "");
        review.setThumbnailUrl(json.containsKey("thumbnailUrl") && !json.isNull("thumbnailUrl")
                ? json.getString("thumbnailUrl") : "");

        String releaseDateString = json.containsKey("releaseDate") && !json.isNull("releaseDate")
                ? json.getString("releaseDate") : null;

        if (releaseDateString != null) {
            try {
                review.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString));
            } catch (ParseException e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        } else {
            review.setReleaseDate(null); // Set to null if not present
        }
        
        review.setMetaScore(json.containsKey("metaScore") && !json.isNull("metaScore")
                ? json.getInt("metaScore") : 0); 
        review.setUserScore(json.containsKey("userScore") && !json.isNull("userScore")
                ? json.getInt("userScore") : 0);         
        review.setCommentCount(json.getInt("commentCount", 0));

		if (json.containsKey("recentUserReviews")) {
            List<UserReview> recentUserReviews = json.getJsonArray("recentUserReviews")
                .getValuesAs(JsonObject.class).stream()
                .map(jsonReview -> {
                    String name = jsonReview.getString("name");
                    String dateString = jsonReview.getString("date");
                    int grade = jsonReview.getInt("grade");
					String body = jsonReview.getString("body");

					// Parse the date string into a Date object
					Date date = null;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
					} catch (ParseException e) {
						e.printStackTrace(); // Handle the exception as needed
					}

                    return new UserReview(name, date, grade, body);
                })
                .collect(Collectors.toList());
			review.setRecentUserReviews(recentUserReviews);
        } else {
            review.setRecentUserReviews(Collections.emptyList());
        }

        return review;
    }

}
