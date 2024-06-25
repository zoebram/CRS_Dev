import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/mainComponentCss/PreferencePage.css';
import { getRecommendedProducts } from '../services/api';

const PreferencePage = () => {
  const [ingredient, setIngredient] = useState('');
  const [flavor, setFlavor] = useState('');
  const [rating, setRating] = useState('');
  const [cocoaPercentage, setCocoaPercentage] = useState('');
  const [includeIngredient, setIncludeIngredient] = useState(false);
  const [mustIncludeAllIngredients, setMustIncludeAllIngredients] = useState(false);
  const [includeFlavor, setIncludeFlavor] = useState(false);
  const [mustIncludeAllFlavors, setMustIncludeAllFlavors] = useState(false);
  const [containsBoth, setcontainsBoth] = useState(false);
  const navigate = useNavigate();

  const handleRecommend = async () => {
    try {
      const params = {
        minRating: rating,
        minCocoaPercent: cocoaPercentage,
        includeIngredients: includeIngredient,
        includeFlavors: includeFlavor,
        ingredientKeywords: ingredient,
        flavorKeywords: flavor,
        ingredientLogic: mustIncludeAllIngredients,
        flavorLogic: mustIncludeAllFlavors,
        combinedLogic: containsBoth,
      };
      console.log('Request parameters:', params);

      const response = await getRecommendedProducts(params);
      console.log('Response status:', response.status);
      console.log('Response data:', response.data);

      if (response.status !== 200) {
        console.error(`HTTP error: ${response.status} ${response.statusText}`);
        throw new Error('Failed to fetch recommendations');
      }

      const results = response.data;
      navigate('/recommend', { state: { results } });
    } catch (error) {
      console.error('Error fetching recommendations:', error);
      // Optionally, update the UI to show an error message
    }
  };

  const handleIncludeIngredientChange = (checked) => {
    setIncludeIngredient(checked);
    if (!checked) {
      setMustIncludeAllIngredients(false);
      setIngredient('');
    }
  };

  const handleMustIncludeAllIngredientsChange = (checked) => {
    if (includeIngredient) {
      setMustIncludeAllIngredients(checked);
    }
  };

  const handleIncludeFlavorChange = (checked) => {
    setIncludeFlavor(checked);
    if (!checked) {
      setMustIncludeAllFlavors(false);
      setFlavor('');
    }
  };

  const handleMustIncludeAllFlavorsChange = (checked) => {
    if (includeFlavor) {
      setMustIncludeAllFlavors(checked);
    }
  };

  const handlecontainsBothChange = (checked) => {
    if (includeIngredient && includeFlavor) {
      setcontainsBoth(checked);
    }
  };

  return (
    <div className="preference-page">
      <h1><strong>What are your preferences?</strong></h1>
      <div className="preference-section">
        <label><strong>Ingredient preference:</strong></label>
        <input type="text" placeholder="ex: sugar, bean" value={ingredient} onChange={(e) => setIngredient(e.target.value)} disabled={!includeIngredient} />
        <div className='checkbox-container'>
          <div className='checkbox-container-child'><input type="checkbox" id="include" checked={includeIngredient} onChange={(e) => handleIncludeIngredientChange(e.target.checked)} />
          <label htmlFor="include">Include ingredient</label></div>
          <div className='checkbox-container-child'><input type="checkbox" id="mustInclude" checked={mustIncludeAllIngredients} onChange={(e) => handleMustIncludeAllIngredientsChange(e.target.checked)} disabled={!includeIngredient} />
          <label htmlFor="mustInclude">Must include all</label></div>
        </div>
      </div>
      <div className="preference-section">
        <label><strong>Flavor preference:</strong></label>
        <input type="text" placeholder="ex: gummy, smokey" value={flavor} onChange={(e) => setFlavor(e.target.value)} disabled={!includeFlavor} />
        <div className='checkbox-container'>
          <div className='checkbox-container-child'><input type="checkbox" id="includeFlavor" checked={includeFlavor} onChange={(e) => handleIncludeFlavorChange(e.target.checked)} />
          <label htmlFor="includeFlavor">Include flavor</label></div>
          <div className='checkbox-container-child'><input type="checkbox" id="mustIncludeFlavor" checked={mustIncludeAllFlavors} onChange={(e) => handleMustIncludeAllFlavorsChange(e.target.checked)} disabled={!includeFlavor} />
          <label htmlFor="mustIncludeFlavor">Must include all</label></div>
        </div>
      </div>
      <div className="preference-section">
        <div className='checkbox-container-child'><input type="checkbox" id="containsBoth" checked={containsBoth} onChange={(e) => handlecontainsBothChange(e.target.checked)} disabled={!includeIngredient || !includeFlavor} />
        <label htmlFor="containsBoth">CONTAINS BOTH INGREDIENT AND FLAVOR</label></div>
      </div>
      <div className="preference-section">
        <label><strong>Rating (1-5):</strong></label>
        <input type="text" placeholder='Default value: 4' value={rating} onChange={(e) => setRating(e.target.value)} />
      </div>
      <div className="preference-section">
        <label><strong>Cocoa Percentage (%):</strong></label>
        <input type="text" placeholder='Default value: 70' value={cocoaPercentage} onChange={(e) => setCocoaPercentage(e.target.value)} />
      </div>
      <button onClick={handleRecommend}>Recommend</button>
    </div>
  );
};

export default PreferencePage;
