import React from 'react';
import './SalesPage.css';

const salesData = [
  {
    id: 1,
    title: "Супер скидка на все игры!",
    description: "Скидка 50% на все игры в нашем магазине. Не упустите шанс обновить свою коллекцию!",
    image: "path/to/image1.jpg",
    buttonText: "Смотреть акции"
  },
  {
    id: 2,
    title: "Подарки к каждой покупке",
    description: "При покупке любой игры вы получаете эксклюзивный подарок!",
    image: "path/to/image2.jpg",
    buttonText: "Узнать больше"
  },
  {
    id: 3,
    title: "Игры месяца",
    description: "Специальные предложения на игры месяца. Скидки до 30%! ",
    image: "path/to/image3.jpg",
    buttonText: "Просмотреть игры"
  }
];

const SalesPage = () => {
  return (
    <div className="sales-page">
      <h1 className="sales-title">Наши акции</h1>
      <div className="sales-container">
        {salesData.map((sale) => (
          <div className="sale-card" key={sale.id}>
            <img src={sale.image} alt={sale.title} className="sale-image" />
            <h2 className="sale-card-title">{sale.title}</h2>
            <p className="sale-card-description">{sale.description}</p>
            <button className="sale-button">{sale.buttonText}</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SalesPage;
