import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SearchReview = () => {
    const [title, setTitle] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        if (title.trim().length >= 2) {
            navigate(`/list?query=${encodeURIComponent(title)}`);
        }
    };

    return (
        <div>
            <h1>Search Movie Reviews</h1>
            <form onSubmit={handleSubmit}>
                <table>
                    <tbody>
                        <tr>
                            <td>Movie name:</td>
                            <td>
                                <input 
                                    type="text" 
                                    size={30} 
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                    placeholder="Title"
                                />
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="right">
                                <button 
                                    type="submit" 
                                    disabled={title.trim().length < 2}
                                >
                                    Search
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    );
};

export default SearchReview;