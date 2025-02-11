import React from 'react';

import { BrowserRouter as Router, Routes, Route } from 'react-router';

import AboutPage from '../pages/AboutPage/AboutPage.tsx';
import LoginPage from '../pages/LoginPage/LoginPage.tsx';

import './App.css';

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path='/' element={ <LoginPage /> } />
                <Route path='/about' element={ <AboutPage /> } />
            </Routes>
        </Router>
    )
}

export default App;