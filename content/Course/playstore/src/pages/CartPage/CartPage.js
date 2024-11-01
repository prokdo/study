import React from 'react';
import './CartPage.css';
import CartItem from '../../components/CartItem/CartItem';

function CartPage(props) {
  const { cartItems, removeFromCart } = props;

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + parseInt(item.price), 0);
  };

  return (
    <div className="cart__content">
      <h1 className="cart__title">Корзина с играми</h1>
      <p className="cart-total">Общая сумма покупки: {calculateTotal()} руб.</p>
      {cartItems.length === 0 ? (
        <p className="cart-empty-message">Ваша корзина пуста. Порадуйте себя чем-нибудь новеньким!</p>
      ) : (
        <div className="cart-items">
          {cartItems.map((item) => (
            <CartItem
              key={item.id}
              item={item}
              removeFromCart={removeFromCart}
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default CartPage;
