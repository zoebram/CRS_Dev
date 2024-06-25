// crs-frontend/src/services/api.js
import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;

export const getProductsByFlavor = (flavorName) => axios.get(`${API_URL}/flavor/${flavorName}`);
export const getProductsByName = (name) => axios.get(`${API_URL}/details/by-name/${name}`);
export const getDetailedProductsByFlavor = (flavorName) => axios.get(`${API_URL}/detailsByFlavor/${flavorName}`);
export const getProductsByCompany = (companyName) => axios.get(`${API_URL}/company/${companyName}`);
export const getDetailedProductsByCompany = (companyName) => axios.get(`${API_URL}/detailsByCompany/${companyName}`);
export const getCompanyName = (companyName) => axios.get(`${API_URL}/companyName/${companyName}`);
export const getProductsByIngredient = (ingredientName) => axios.get(`${API_URL}/ingredient/${ingredientName}`);
export const getDetailedProductsByIngredient = (ingredientName) => axios.get(`${API_URL}/details/ingredient/${ingredientName}`);
export const getRecommendedProducts = (options) => axios.get(`${API_URL}/recommended`, { params: options });
export const getProductsDetailsById = (id) => axios.get(`${API_URL}/details/by-id/${id}`);