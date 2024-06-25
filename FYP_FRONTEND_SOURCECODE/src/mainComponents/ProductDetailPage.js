import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/mainComponentCss/ProductDetailPage.css';
import { getProductsByCompany, getProductsByFlavor, getProductsByIngredient, getProductsDetailsById } from '../services/api';

const ProductDetailPage = () => {
  const { itemId } = useParams();
  const encodedItemId = encodeURIComponent(itemId); // Encode the itemId to handle special characters
  const [product, setProduct] = useState(null);
  const [error, setError] = useState(null); // State to track errors
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const result = await getProductsDetailsById(encodedItemId); // Use encoded itemId in API call
        if (result.data) {
          setProduct(result.data);
        } else {
          console.log("No product found with ID:", itemId);
          throw new Error("No product found with the provided ID."); // Throw an error if no product is found
        }
      } catch (error) {
        console.error("Error fetching product details:", error);
        navigate('/error', { state: { message: 'Failed to process the search request.' } });
      }
    };

    fetchProduct();
  }, [encodedItemId]);

  const handleCompanyClick = async (company) => {
    try {
      const encodedCompany = encodeURIComponent(company);
      const result = await getProductsByCompany(encodedCompany);
      navigate('/search-results', { state: { results: result.data, searchQuery: company, searchType: 'company' } });
    } catch (error) {
      console.error("Error fetching products by company:", error);
      navigate('/error', { state: { message: 'Failed to process the search request.' } });
    }
  };

  const handleFlavorClick = async (flavor) => {
    try {
      const encodedFlavor = encodeURIComponent(flavor);
      const result = await getProductsByFlavor(encodedFlavor);
      navigate('/search-results', { state: { results: result.data, searchQuery: flavor, searchType: 'flavor' } });
    } catch (error) {
      console.error("Error fetching products by flavor:", error);
      navigate('/error', { state: { message: 'Failed to process the search request.' } });
    }
  };

  const handleIngredientClick = async (ingredient) => {
    try {
      const encodedIngredient = encodeURIComponent(ingredient);
      const result = await getProductsByIngredient(encodedIngredient);
      navigate('/search-results', { state: { results: result.data, searchQuery: ingredient, searchType: 'ingredient' } });
    } catch (error) {
      console.error("Error fetching products by ingredient:", error);
      navigate('/error', { state: { message: 'Failed to process the search request.' } });
    }
  };

  if (error) {
    throw error; // Throw the error to be caught by ErrorBoundary
  }

  if (!product) {
    return <div>Loading...</div>;
  }

  return (
    <div className="product-detail-page">
      <h2>{product[0].name}</h2>
      <p>{product[0].description}</p>
      <div className="product-info">
        <div className="info-box">
          <p>This product's cocoa bean originates from <b>{product[0].origin}</b></p>
          <p>The company that makes this product is <button onClick={() => handleCompanyClick(product[0].company)} className="link-button"><b>{product[0].company}</b></button></p>
          <p>It has the flavor(s) of: {product[0].flavors?.map((flavor, index) => (
            <li key={index}>
              <button onClick={() => handleFlavorClick(flavor)} className="link-button"><b>{flavor}</b></button>
            </li>
          ))}</p>
          <p>The percentage of cocoa content in this product is <b>{product[0].cocoaPercent}%</b></p>
          <p>The rating of this product is: <b>{product[0].rating} / 5</b></p>
          <p>The total number of ingredients in this product is: <b>{product[0].ingredients?.length}</b></p>
        </div>
        <div className="ingredients-box">
          <div className='ingredients-box-content'>
            <h4>Ingredients:</h4>
            <p>The ingredients in this product are:</p>
            <ul>
              {product[0].ingredients?.map((ingredient, index) => (
                <li key={index}>
                  <button onClick={() => handleIngredientClick(ingredient)} className="link-button"><b>{ingredient}</b></button>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailPage;
