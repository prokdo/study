import React from 'react';
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer__contact">
        <h3>Контакты</h3>
        <p>Адрес: пл. Гагарина, 1, г. Ростов-на-Дону</p>
        <p>Телефон: +7 (800) 555-35-35</p>
        <p>Email: support@playstore.com</p>
      </div>
      <div className="footer__social">
        <h3>Мы в соцсетях</h3>
        <ul>
          <li><a href="https://web.telegram.org/a/">Telegram</a></li>
          <li><a href="https://vk.com/">Вконтакте</a></li>
          <li><a href="https://instagram.com">Instagram</a></li>
        </ul>
      </div>
      <div className="footer__copyright">
        <p>&copy; 2024 PlayStore. Все права защищены.</p>
      </div>
    </footer>
  );
}

export default Footer;
