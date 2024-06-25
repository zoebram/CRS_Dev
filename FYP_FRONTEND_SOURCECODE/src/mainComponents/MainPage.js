import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/mainComponentCss/MainPage.css';
import SearchBarComponent from '../mainComponents/components/SearchBarComponent';
import { getProductsByCompany, getProductsByFlavor, getProductsByIngredient, getProductsByName } from '../services/api';

const MainPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchType, setSearchType] = useState('flavor'); // Default search type
  const navigate = useNavigate();

  const handleSearchQueryChange = (e) => {
    if (e && e.target) {
      setSearchQuery(e.target.value); // Correctly handle the input change
    }
  };

  const handleSearch = async () => {
    if (searchQuery.trim()) {
      // Check for invalid input
      if (searchQuery.includes("/")) {
        alert("The search query cannot contain the '/' character.");
        return;
      }
      if (searchQuery.trim() === "." || /^\.+$/.test(searchQuery)) {
        alert("The search query cannot consist only of '.' characters.");
        return;
      }

      try {
        let result;
        switch (searchType) {
          case 'flavor':
            result = await getProductsByFlavor(searchQuery);
            break;
          case 'name':
            result = await getProductsByName(searchQuery);
            break;
          case 'company':
            result = await getProductsByCompany(searchQuery);
            break;
          case 'ingredient':
            result = await getProductsByIngredient(searchQuery);
            break;
          default:
            return;
        }
        if (!result?.data || result.data.length === 0) {
          alert('No results found');
        } else {
          // Handle successful search with results
          navigate('/search-results', { state: { results: result.data, searchQuery, searchType } });
        }
      } catch (error) {
        console.error("Search error:", error);
        navigate('/error', { state: { message: 'Failed to process the search request.' } });
      }
    } else {
      alert('Please enter any search query.');
    }
  };

  return (
    <div className="main-page">
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
      <div className="think-big">
        Think Big. Think CRS
      </div>
      <div>
        <div className="semi-circle-one"></div>
        <div className="semi-circle-two"></div>
      </div>
    </div>
  );
};

export default MainPage;