import React from 'react';

import { BrowserRouter as Router, Routes, Route } from 'react-router';

import HomePage from '../pages/HomePage/HomePage.tsx';
import AboutPage from '../pages/AboutPage/AboutPage.tsx';

import './App.css';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={ <HomePage /> } />
        <Route path="/about" element={ <AboutPage /> } />
      </Routes>
    </Router>
  );
}

export default App;
