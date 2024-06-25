import React from 'react';
import { Link } from 'react-router-dom';
import '../../css/componentCss/HeaderComponent.css';

const HeaderComponent = () => {
  return (
    <header className="header">
      <h1>
        <nav className='header-title'>
          <Link to="/">CRS</Link>
        </nav>
      </h1>
      <nav>
        <Link to="/">Home</Link>
        <Link to="/preferences">Recommend</Link>
      </nav>
    </header>
  );
};

export default HeaderComponent;
