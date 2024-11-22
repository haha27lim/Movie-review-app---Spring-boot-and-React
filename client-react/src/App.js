import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import SearchReview from './components/SearchReview';
import MovieReviewsList from './components/MovieReviewsList';
import PostComment from './components/PostComment';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchReview />} />
        <Route path="/list" element={<MovieReviewsList />} />
        <Route path="/comment/:movieName" element={<PostComment />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
