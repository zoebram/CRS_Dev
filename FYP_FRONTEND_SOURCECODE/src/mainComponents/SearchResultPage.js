import React, { useEffect, useState } from 'react';
import Pagination from 'react-bootstrap/Pagination';
import { useLocation, useNavigate } from 'react-router-dom';
import '../css/mainComponentCss/SearchResultPage.css';
import { getProductsByCompany, getProductsByFlavor, getProductsByIngredient, getProductsByName } from '../services/api';
import SearchBarComponent from './components/SearchBarComponent';

const SearchResultsPage = () => {
  const location = useLocation();
  const { results, searchQuery: initialSearchQuery = '', searchType: initialSearchType = '' } = location.state || { results: [] };
  const [currentPage, setCurrentPage] = useState(1);
  const [screenWidth, setScreenWidth] = useState(window.innerWidth);

  const [searchQuery, setSearchQuery] = useState(initialSearchQuery.toString());
  const [searchType, setSearchType] = useState(initialSearchType);
  const [submittedSearchType, setSubmittedSearchType] = useState(initialSearchType);
  const navigate = useNavigate();

  useEffect(() => {
    if (!['flavor', 'name', 'company', 'ingredient'].includes(searchType)) {
      setSearchType('flavor'); // Default to 'flavor' or navigate back or to an error page
    }
  }, [searchType]);

  const handleSearchQueryChange = (e) => {
    if (e && e.target) {
      setSearchQuery(e.target.value || ''); // Fallback to empty string if undefined
    } else {
      console.error('Event or event target is undefined');
      navigate('/error', { state: { message: 'Failed to process the search request.' } });
    }
  };

  const handleSearch = async () => {
    if (searchQuery.trim()) { // Now searchQuery is guaranteed to be a string
      // Check for invalid input
      if (searchQuery.includes("/")) {
        alert("The search query cannot contain the '/' character.");
        return;
      }
      if (searchQuery.trim() === "." || /^\.+$/.test(searchQuery)) {
        alert("The search query cannot consist only of '.' characters.");
        return;
      }
    }

    try {
      if (searchQuery.trim()) {
        const encodedSearchQuery = encodeURIComponent(searchQuery.trim());
        let result;
        switch (searchType) {
          case 'flavor':
            result = await getProductsByFlavor(encodedSearchQuery);
            break;
          case 'name':
            result = await getProductsByName(encodedSearchQuery);
            break;
          case 'company':
            result = await getProductsByCompany(encodedSearchQuery);
            break;
          case 'ingredient':
            result = await getProductsByIngredient(encodedSearchQuery);
            break;
          default:
            return;
        }
        if (result?.data) {
          navigate('/search-results', { state: { results: result.data } });
          setSubmittedSearchType(searchType);
        } else {
          alert('No results found');
        }
      } else {
        alert('Please enter any search query.');
      }
    } catch (error) {
      console.error('Search failed:', error);
      navigate('/error', { state: { message: 'Failed to process the search request.' } });
    }
  };

  // Function to calculate items per page based on screen width
  const getItemsPerPage = () => {
    if (screenWidth > 2560) return 16;
    else if (screenWidth > 1920) return 12; 
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
    <div className='search-result-page'>
      <div className="search-page">
        <SearchBarComponent value={searchQuery} onChange={handleSearchQueryChange} onSearch={handleSearch} />
        <div className="search-type-selector">
  <label>
    <input
      type="radio"
      value="flavor"
      checked={searchType === 'flavor'}
      onChange={(e) => setSearchType(e.target.value)}
    />
    <span>Flavor</span> 
  </label>
  <label>
    <input
      type="radio"
      value="name"
      checked={searchType === 'name'}
      onChange={(e) => setSearchType(e.target.value)}
    />
    <span>Name</span>
  </label>
  <label>
    <input
      type="radio"
      value="company"
      checked={searchType === 'company'}
      onChange={(e) => setSearchType(e.target.value)}
    />
    <span>Company</span> 
  </label>
  <label>
    <input
      type="radio"
      value="ingredient"
      checked={searchType === 'ingredient'}
      onChange={(e) => setSearchType(e.target.value)}
    />
    <span>Ingredient</span> 
  </label>
</div>
    </div>
      <h1>Search Results by {submittedSearchType.charAt(0).toUpperCase() + submittedSearchType.slice(1)}</h1>
      {results.length > 0 ? (
        <div className='search-results'>
          {currentItems.map((item, index) => (
            <button 
              key={index} 
              className="result-item-box" 
              onClick={() => handleItemClick(item.id)}
              onKeyDown={(e) => e.key === 'Enter' && handleItemClick(item.id)}
            >
              <div className='item-title'>
                <h3 className='item-name'>{item.name}</h3>
              </div>
              <div className='item-overview'>
                <h5 className='product-overview'>Product Overview</h5>
                <p className='cocoa-percent'>Cocoa Percentage:<b> {item.cocoaPercent}% </b> </p>
                <p className='number-of-ingredients'>Number of Ingredients: <b>{item.numberOfIngredients}</b></p>
                <p className='product-rating'>Product rating: <b>{item.rating}</b></p>
              </div>
            </button>
          ))}
        </div>
      ) : (
        <p>No results found.</p>
      )}
      <div className="pagination-container">
        <Pagination>
          <Pagination.First onClick={() => setCurrentPage(1)} disabled={currentPage === 1} />
          <Pagination.Prev onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1} />
          <span>Page <input type="number" value={currentPage} onChange={handlePageInput} style={{ width: '60px' }} /> of {pageCount}</span>
          <Pagination.Next onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === pageCount} />
          <Pagination.Last onClick={() => setCurrentPage(pageCount)} disabled={currentPage === pageCount} />
        </Pagination>
      </div>
    </div>
  );
};

export default SearchResultsPage;