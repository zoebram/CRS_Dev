import PropTypes from 'prop-types'; // Import PropTypes
import React from 'react';

const ErrorPage = ({ message }) => {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Error Occurred :( </h1>
      <p>{message}</p>
      <div>Return to Home</div>
    </div>
  );
};

ErrorPage.propTypes = {
  message: PropTypes.string.isRequired, // Add prop validation for message
};

export default ErrorPage;