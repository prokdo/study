import React from 'react';
import { Link } from 'react-router-dom';
import './Menu.css';

function Menu() {
  return (
    <nav className="menu">
      <ul>
        <li><Link to="/">Главная</Link></li>
        <li><Link to="/games">Игры</Link></li>
        <li><Link to="/sales">Акции</Link></li>
      </ul>
    </nav>
  );
}

export default Menu;
