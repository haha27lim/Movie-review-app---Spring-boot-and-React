import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { MovieService } from '../services/MovieService';

const PostComment = () => {
    const { movieName } = useParams(); // This will get the movie title from the URL
    const [name, setName] = useState('');
    const [rating, setRating] = useState(1);
    const [comment, setComment] = useState('');
    const navigate = useNavigate();

    const isFormValid = name.trim().length >= 3 && comment.trim() !== '';

    const handlePostComment = async (e) => {
        e.preventDefault();
        if (isFormValid) {
            await MovieService.postComment({ title: movieName, name, rating, comment });
            navigate(`/list?query=${movieName}`);
        }
    };

    return (
        <div>
            <h1>Post Comment for Movie: {movieName}</h1>
            <form onSubmit={handlePostComment}>
                <table>
                    <tr>
                        <td><label>Name: </label></td>
                        <td><input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Your Name" /></td>
                    </tr>
                    <tr>
                        <td><label>Rating: </label></td>
                        <td><select value={rating} onChange={(e) => setRating(e.target.value)}>
                                {[1, 2, 3, 4, 5].map((r) => (
                                    <option key={r} value={r}>{r}</option>
                                ))}
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Comment: </label></td>
                        <td><textarea value={comment} onChange={(e) => setComment(e.target.value)} placeholder="Your Comment"></textarea></td>
                    </tr>
                    <tr>
                        <td><button type="button" onClick={() => navigate(`/list?query=${movieName}`)}>Back</button></td>
                        <td><button type="submit" disabled={!isFormValid}>Post</button></td>
                    </tr>
                </table>
            </form>
        </div>
    );
};

export default PostComment;