import React from 'react';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-page">
      <div className="home-hero">
        <h1 className="home-title">PlayStore</h1>
      </div>
      <div className="home-content">
        <p className="home-text">
          Добро пожаловать в <strong>PlayStore</strong> – место, где страсть к играм соединяется с качественным сервисом! Мы – команда энтузиастов, которые с детства были влюблены в видеоигры и виртуальные миры. Наша миссия – предоставить вам доступ к лучшим игровым продуктам, от новинок до классических хитов, по доступным ценам.
        </p>
        <h2 className="home-subtitle">Почему выбирают нас?</h2>
        <ul className="home-list">
          <li>Широкий ассортимент игр для всех популярных платформ.</li>
          <li>Удобные способы оплаты и быстрая доставка.</li>
          <li>Качественное обслуживание и поддержка клиентов.</li>
          <li>Гарантированная безопасность каждой покупки.</li>
        </ul>
        <h2 className="home-subtitle">Наша цель</h2>
        <p className="home-text">
          Мы не просто продаем игры – мы создаем сообщество игроков, объединенных любовью к виртуальным мирам. Наши клиенты – это наши единомышленники, с которыми мы делимся последними новостями и рекомендациями.
        </p>
        <p className="home-text">
          Присоединяйтесь к нашему сообществу, чтобы оставаться на пике игровой индустрии вместе с <strong>PlayStore</strong>!
        </p>
      </div>
    </div>
  );
};

export default HomePage;