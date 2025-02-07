import React from 'react';

import NavigationButton from '../../components/NavigationButton/NavigationButton.tsx';

import './HomePage.css';

const HomePage: React.FC = () => {
  return (
    <div className="home-container">
      <div className="card">
        <h1>Распределенные информационные системы</h1>
        <h2>Лабораторная работа №1</h2>
        <p>Автор: Прокопенко Д.О, ВПР42</p>
        <NavigationButton path="/about" label="О проекте" />
      </div>
    </div>
  );
};

export default HomePage;
