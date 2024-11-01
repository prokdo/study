import React from 'react';
import logo from '../../assets/images/Header/logo.png';
import './Header.css';
import { Link } from 'react-router-dom';

function Header({ cartItemCount }) {
  return (
    <header className="header">
      <div className="header__logo">
        <img src={logo} alt="GAME STORE" />
        <div className="header_name">
          <p>PLAY</p>
          <p>STORE</p>
        </div>
      </div>
      <Link to="/cart" className="header__cart">
        {cartItemCount > 0 && <span className="cart-item-count">{cartItemCount}</span>}
      </Link>
    </header>
  );
}

export default Header;
