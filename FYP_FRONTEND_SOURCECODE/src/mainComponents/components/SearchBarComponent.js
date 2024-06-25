import React from "react";
import "../../css/componentCss/SearchBar.css";

const SearchBarComponent = ({ value, onChange, onSearch }) => {
    return (
        <div className="search-bar">
            <input 
                type="text" 
                placeholder="Search Anything Cocoa" 
                value={value}
                onChange={onChange}  // This should correctly pass the event object
                onKeyDown={(e) => e.key === 'Enter' && onSearch()}
            />
            <button onClick={onSearch}>Search</button>
        </div>
    );
};

export default SearchBarComponent;