import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { MovieService } from '../services/MovieService';
import '../components/MovieReviewsList.css';
import placeholder from '../assets/images/placeholder.jpg';

const MovieReviewsList = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [reviews, setReviews] = useState([]);
    const [movieTitle, setMovieTitle] = useState(''); // State to store the movie title

    useEffect(() => {
        const query = new URLSearchParams(location.search).get('query');
        if (query) {
            const fetchReviews = async () => {
                try {
                    const data = await MovieService.getSearch(query);
                    if (data) {
                        setReviews([data]); // Wrap the single review in an array
                        setMovieTitle(query); // Store the query as the movie title
                    } else {
                        setReviews([]);
                    }
                } catch (error) {
                    console.error("Error fetching reviews:", error);
                }
            };
            fetchReviews();
        }
    }, [location.search]);

    if (reviews.length === 0) {
        return <div><h2>Search Result</h2><p>Your search produces no result.</p></div>;
    }

    return (
        <div>
            <div style={{display: 'flex', alignItems: 'center'}}>
                <h2 style={{marginRight:10}}>Search Result</h2>
                <button onClick={() => navigate('/')}>Back</button>
            </div>
            <div>
            {reviews.map((review) => (
                <div key={review.title || "untitled-review"}>
                    <h2>
                        <a href={review.articleUrl} target="_blank" rel="noopener noreferrer">
                            {review.title || "Untitled Movie"} {/* Fallback for null title */}
                        </a>
                    </h2>
                    <img src={review.thumbnailUrl || placeholder} alt={review.title || "Placeholder"} /> {/* Fallback for thumbnail */}
                    <p>Director: {review.director}</p>
                    <p>Description: {review.description}</p>
                    <p>Genre: {review.genre}</p>
                    <p>UserScore: {review.userScore}</p>
                    <p>Rating: {review.metaScore || "N/A"}</p>
                    <h3>Recent User Reviews</h3>
                    {review.recentUserReviews && review.recentUserReviews.map((userReview, index) => (
                        <div key={index} className="article">
                            <p>Name: {userReview.name}</p>
                            <p>Date: {userReview.date}</p>
                            <p>Grade: {userReview.grade}</p>
                            <p>Body: {userReview.body}</p>
                        </div>
                    ))}
                    <p>Comments: {review.commentCount || 0}</p>                    
                    <button onClick={() => navigate(`/comment/${movieTitle}`)} className="comment">Comment</button>
                </div>
            ))}            
            </div>
        </div>
    );
};

export default MovieReviewsList;