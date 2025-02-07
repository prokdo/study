import React from 'react'

import NavigationButton from '../../components/NavigationButton/NavigationButton.tsx';

import './AboutPage.css';

const info = [
    {
        name: 'Docker',
        description: 'Облегчённый контейнеризированный образ node@current-alpine3.20 на базе Node.js и Alpine Linux. Обеспечивает минималистичную, быструю и безопасную среду для разработки и развертывания веб-приложений',
        icon: '🐳'
    },
    {
        name: 'TypeScript',
        description: 'Язык программирования, расширяющий JavaScript за счёт статической типизации. Улучшает качество кода, делает разработку удобнее и снижает количество ошибок',
        icon: '🔷'
    },
    {
        name: 'React',
        description: 'Популярная библиотека для создания динамичных и отзывчивых пользовательских интерфейсов. Позволяет строить приложения на основе компонентов и эффективно управлять состоянием',
        icon: '⚛️'
    },
    {
        name: 'React Router',
        description: 'Библиотека для маршрутизации в React-приложениях. Позволяет реализовывать навигацию между страницами без перезагрузки, обеспечивая плавный пользовательский опыт',
        icon: '🛣️'
    }
];

const AboutPage: React.FC = () => {
    return (
        <div className="info-container">
            <h1>Используемые технологии</h1>
            <div className="info-grid">
                { info.map( (value, index) => (
                <div className="info-card" key={index}>
                    <div className="info-icon">{value.icon}</div>
                    <h2>{value.name}</h2>
                    <p>{value.description}</p>
                </div>
                ) ) }
            </div>
            <NavigationButton path='/' label='Назад' />
        </div>
    )
}

export default AboutPage;