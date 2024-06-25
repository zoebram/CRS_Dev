import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import './css/common.css';
import ErrorPage from './mainComponents/ErrorPage';
import MainPage from './mainComponents/MainPage';
import PreferencePage from './mainComponents/PreferencePage';
import ProductDetailPage from './mainComponents/ProductDetailPage';
import RecommendationPage from './mainComponents/RecommendationPage';
import SearchResultPage from './mainComponents/SearchResultPage';
import HeaderComponent from './mainComponents/components/HeaderComponent';

const App = () => {
  return (
    <Router>
      <div>
        <HeaderComponent />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/product-details/:itemId" element={<ProductDetailPage />} />
          <Route path="/recommend" element={<RecommendationPage />} />
          <Route path="/search-results" element={<SearchResultPage />} />
          <Route path="/preferences" element={<PreferencePage />} />
          <Route path="/error" element={<ErrorPage />} />
          
        </Routes>
      </div>
    </Router>
  );
};

export default App;
