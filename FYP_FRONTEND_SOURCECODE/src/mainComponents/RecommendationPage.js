import React, { useEffect, useState } from 'react';
import Pagination from 'react-bootstrap/Pagination';
import { useLocation, useNavigate } from 'react-router-dom';

const RecommendationPage = () => {
  const location = useLocation();
  const { results } = location.state || { results: [] };
  const [currentPage, setCurrentPage] = useState(1);
  const [screenWidth, setScreenWidth] = useState(window.innerWidth);
  const [error, setError] = useState(null);  // State to track errors
  const navigate = useNavigate();

  const getItemsPerPage = () => {
    if (screenWidth > 2560) return 18;
    else if (screenWidth > 1920) return 16; 
    else if (screenWidth > 1080) return 8; 
    else if (screenWidth <= 768) return 4; 
    else return 4; // Phones
  };

  const [itemsPerPage, setItemsPerPage] = useState(getItemsPerPage());

  useEffect(() => {
    const handleResize = () => {
      setScreenWidth(window.innerWidth);
      setItemsPerPage(getItemsPerPage());
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, [screenWidth]);

  useEffect(() => {
    // Simulate fetching data and catching an error
    try {
      // Simulated fetch logic here
      if (!results) throw new Error('Failed to load recommendations');
    } catch (err) {
      setError(err);
    }
  }, [results]);

  if (error) {
    throw error;  // Throw the error to be caught by ErrorBoundary
  }

  const pageCount = Math.ceil(results.length / itemsPerPage);
  const currentItems = results.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  const handlePageInput = (e) => {
    const page = parseInt(e.target.value, 10);
    if (page >= 1 && page <= pageCount) {
      setCurrentPage(page);
    }
  };

  const handleItemClick = (itemId) => {
    console.log("Navigating to ID:", itemId);
    navigate(`/product-details/${encodeURIComponent(itemId)}`);
  };

  return (
    <div className="recommendation-page">
      <h1>Recommendations</h1>
      {results.length > 0 ? (
        <div className='search-results'>
        {currentItems.map((item, index) => (
          <button 
            key={index} 
            className="result-item-box" 
            onClick={() => handleItemClick(item.id)}
            onKeyDown={(e) => e.key === 'Enter' && handleItemClick(item.id)}
          >
            <h3 className='item-name'>{item.name}</h3>
            <h5 className='product-overview'>Product Overview</h5>
            <p className='cocoa-percent'>Cocoa Percentage:<span> {item.cocoaPercent}% </span> </p>
            <p className='number-of-ingredients'>Number of Ingredients: {item.numberOfIngredients}</p>
            <p className='product-rating'>Product rating: {item.rating}</p>
          </button>
        ))}
      </div>
      ) : (
        <p>No recommendations found based on your preferences.</p>
      )}
      <div className="pagination-container">
        <Pagination>
          <Pagination.First onClick={() => setCurrentPage(1)} disabled={currentPage === 1} />
          <Pagination.Prev onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1} />
          <span>Page <input type="number" value={currentPage} onChange={(e) => setCurrentPage(Number(e.target.value))} style={{ width: '60px' }} /> of {pageCount}</span>
          <Pagination.Next onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === pageCount} />
          <Pagination.Last onClick={() => setCurrentPage(pageCount)} disabled={currentPage === pageCount} />
        </Pagination>
      </div>
    </div>
  );
};

export default RecommendationPage;