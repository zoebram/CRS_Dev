import React from 'react';

function ProductResultComponent({ result, onClick }) {
  return (
    <div className="product-result-item" onClick={() => onClick(result)}>
      <h3>{result.name}</h3>
      <p>{result.description}</p>
    </div>
  );
}

export default ProductResultComponent;
