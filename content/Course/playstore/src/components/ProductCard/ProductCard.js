import React, { useState } from 'react';
import './ProductCard.css';
import Rating from 'react-rating-stars-component';

function ProductCard(props) {
  const { cover, title, release_date, rating, genre, price, onBuy } = props;
  const [userRating, setUserRating] = useState(null);

  const handleBuyClick = () => {
    onBuy({ cover, title, release_date, rating, genre, price, userRating });
  };

  const handleRatingChange = (newRating) => {
    setUserRating(newRating);
  };

  return (
    <div className="game-card">
      <img src={cover} alt={title} className="game-card__cover" />
      <div className="game-card__info">
        <h2 className="game-card__title">{title}</h2>
        <p className="game-card__genre">Жанр: {genre}</p>
        <p className="game-card__release_date">Год выпуска: {release_date}</p>
        <p className="game-card__rating">Рейтинг: {rating}</p>
        <p className="game-card__price">Цена: {price} руб.</p>
        <div className="game-card__user-rating">
          <Rating
            count={5}
            size={24}
            onChange={handleRatingChange}
            value={userRating}
            activeColor="#ffd700"
          />
        </div>
        <button className="game-card__button" onClick={handleBuyClick}>Купить</button>
      </div>
    </div>
  );
}

export default ProductCard;
