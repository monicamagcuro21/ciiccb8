// src/index.js

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import './index.css'; // global styles if any

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// --- Web Vitals Reporting ---
reportWebVitals(console.log); // logs CLS, FID, LCP, FCP, TTFB to console
