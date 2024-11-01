import React, { useState, useEffect } from 'react';
import ProductCard from '../../components/ProductCard/ProductCard';
import './ProductPage.css';
import cover1 from '../../assets/images/ProductCard/cover.png';
import cover2 from '../../assets/images/ProductCard/cover.png';
import cover3 from '../../assets/images/ProductCard/cover.png';
import cover4 from '../../assets/images/ProductCard/cover.png';
import cover5 from '../../assets/images/ProductCard/cover.png';
import cover6 from '../../assets/images/ProductCard/cover.png';
import cover7 from '../../assets/images/ProductCard/cover.png';
import cover8 from '../../assets/images/ProductCard/cover.png';
import cover9 from '../../assets/images/ProductCard/cover.png';
import cover10 from '../../assets/images/ProductCard/cover.png';
import cover11 from '../../assets/images/ProductCard/cover.png';
import cover12 from '../../assets/images/ProductCard/cover.png';

function ProductPage({ addToCart }) {
  const [selectedGenre, setSelectedGenre] = useState("all");
  const [sortBy, setSortBy] = useState("");
  const [cartItemCount, setCartItemCount] = useState(0);

  useEffect(() => {
    const storedCartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    setCartItemCount(storedCartItems.length);
  }, []);

  const handleGenreChange = (event) => {
    setSelectedGenre(event.target.value);
  };

  const handleSortByChange = (event) => {
    setSortBy(event.target.value);
  };

  const handleBuyClick = (game) => {
      addToCart(game);
  };

  const filterGamesByGenre = (game) => {
    if (selectedGenre === "all") {
      return true;
    } else {
      return game.genre === selectedGenre;
    }
  };

  const sortGames = (games) => {
    if (sortBy === "priceAsc") {
      return games.slice().sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
    } else if (sortBy === "priceDesc") {
      return games.slice().sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
    } else if (sortBy === "ratingDesc") {
      return games.slice().sort((a, b) => parseFloat(b.rating) - parseFloat(a.rating));
    } else {
      return games;
    }
  };

  const games = [
    { cover: cover1, title: "Игра 1", release_date: "2004", rating: "4.5", genre: "Экшен", price: "1299" },
    { cover: cover2, title: "Игра 2", release_date: "2004", rating: "4.7", genre: "Приключения", price: "999" },
    { cover: cover3, title: "Игра 3", release_date: "2004", rating: "5.0", genre: "Ролевые игры", price: "1499" },
    { cover: cover4, title: "Игра 4", release_date: "2004", rating: "4.2", genre: "Стратегия", price: "799" },
    { cover: cover5, title: "Игра 5", release_date: "2004", rating: "4.8", genre: "Спорт", price: "1599" },
    { cover: cover6, title: "Игра 6", release_date: "2004", rating: "3.9", genre: "Головоломка", price: "499" },
    { cover: cover7, title: "Игра 7", release_date: "2004", rating: "4.6", genre: "Хоррор", price: "1199" },
    { cover: cover8, title: "Игра 8", release_date: "2004", rating: "4.1", genre: "Приключения", price: "1099" },
    { cover: cover9, title: "Игра 9", release_date: "2004", rating: "5.0", genre: "Экшен", price: "1299" },
    { cover: cover10, title: "Игра 10", release_date: "2004", rating: "4.3", genre: "Ролевые игры", price: "1399" },
    { cover: cover11, title: "Игра 11", release_date: "2004", rating: "4.9", genre: "Спорт", price: "1599" },
    { cover: cover12, title: "Игра 12", release_date: "2004", rating: "4.0", genre: "Головоломка", price: "599" },
  ];

  return (
    <div className="page">
      <h1>Ассортимент видеоигр</h1>
      <div className="filters">
        <label>Жанр: </label>
        <select value={selectedGenre} onChange={handleGenreChange}>
          <option value="all">Все</option>
          <option value="Экшен">Экшен</option>
          <option value="Приключения">Приключения</option>
          <option value="Ролевые игры">Ролевые игры</option>
          <option value="Стратегия">Стратегия</option>
          <option value="Спорт">Спорт</option>
          <option value="Головоломка">Головоломка</option>
          <option value="Хоррор">Хоррор</option>
        </select>
        <label>Сортировка: </label>
        <select value={sortBy} onChange={handleSortByChange}>
          <option value="">Нет</option>
          <option value="priceAsc">По цене (возрастание)</option>
          <option value="priceDesc">По цене (убывание)</option>
          <option value="ratingDesc">По рейтингу</option>
        </select>
      </div>
      <div className="game-card-container">
        {sortGames(games.filter(filterGamesByGenre)).map((game) => (
          <ProductCard
            key={game.id}
            cover={game.cover}
            title={game.title}
            release_date={game.release_date}
            rating={game.rating}
            genre={game.genre}
            price={game.price}
            onBuy={() => handleBuyClick(game)}
          />
        ))}
      </div>
    </div>
  );
}

export default ProductPage;



