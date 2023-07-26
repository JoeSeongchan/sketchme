/* eslint-disable import/no-named-as-default */
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './App';
// eslint-disable-next-line import/no-named-as-default-member

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // eslint-disable-next-line react/jsx-filename-extension
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
);
