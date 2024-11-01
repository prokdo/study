import React from 'react';
import './CartItem.css';

function CartItem({ item, index, removeFromCart }) {
  const { cover, title, genre, release_date, price } = item;

  const handleRemoveClick = () => {
    removeFromCart(index);
  }

  return (
    <div className="cart-item">
      <img src={cover} alt={title} className="cart-item__cover" />
      <div className="cart-item__info">
        <h3 className="cart-item__title">{title}</h3>
        <p className="cart-item__genre">Жанр: {genre}</p>
        <p className="cart-item__release_date">Год выпуска: {release_date}</p>
        <p className="cart-item__price">Цена: {price} руб.</p>
        <button className="cart-item__remove-button" onClick={handleRemoveClick}>Удалить</button>
      </div>
    </div>
  );
}

export default CartItem;
