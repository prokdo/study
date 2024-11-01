import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import Menu from './components/Menu/Menu';
import ProductPage from './pages/ProductPage/ProductPage';
import SalesPage from './pages/SalesPage/SalesPage';
import CartPage from './pages/CartPage/CartPage';
import HomePage from './pages/HomePage/HomePage';
import './App.css';

function App() {
  const [cartItems, setCartItems] = useState([]);
  const [cartItemCount, setCartItemCount] = useState(0);

  const addToCart = (item) => {
    const storedCartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

    storedCartItems.push(item)

    localStorage.setItem('cartItems', JSON.stringify(storedCartItems));
    setCartItems(storedCartItems);
    setCartItemCount(cartItemCount + 1);
  };

  const removeFromCart = (index) => {
    const storedCartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    const newCartItems = [...storedCartItems];

    newCartItems.splice(index, 1);

    localStorage.setItem('cartItems', JSON.stringify(newCartItems));
    setCartItems(newCartItems);
    setCartItemCount(cartItemCount - 1);
  };

  useEffect(() => {
    const storedCartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    setCartItems(storedCartItems);
    setCartItemCount(storedCartItems.length);
  }, []);

  return (
    <Router>
      <div className="app">
        <Header cartItemCount={cartItemCount} />
        <Menu />
        <Routes>
          <Route path="/" element={<HomePage/>} />
          <Route path="/sales" element={<SalesPage />} />
          <Route path="/games" element={<ProductPage addToCart={addToCart} />} />
          <Route path="/cart" element={<CartPage cartItems={cartItems} removeFromCart={removeFromCart} />} />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
