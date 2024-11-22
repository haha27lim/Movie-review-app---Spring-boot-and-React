import axios from 'axios';

const SEARCH_REVIEWS_URI = "http://localhost:8080/api/search";
const COMMENTS_URI = "http://localhost:8080/api/comment";

export const MovieService = {
    getSearch: async (title) => {
        const response = await axios.get(`${SEARCH_REVIEWS_URI}/${title}`, {
            params: { reviews: true }
        });
        return response.data;
    },

    postComment: async (comment) => {
        const params = new URLSearchParams();
        params.append('title', comment.title);
        params.append('name', comment.name);
        params.append('rating', comment.rating.toString());
        params.append('comment', comment.comment);

        const response = await axios.post(COMMENTS_URI, params, {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });
        return response.data;
    }
};