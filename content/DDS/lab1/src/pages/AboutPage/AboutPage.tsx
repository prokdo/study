import React from 'react'

import InfoCard from '../../components/InfoCard/InfoCard.tsx';
import NavigationButton from '../../components/NavigationButton/NavigationButton.tsx';

import './AboutPage.css';

const info = [
    {
        icon: '🐳',
        header: 'Docker',
        content: 'Облегчённый контейнеризированный образ node@current-alpine3.20 на базе Node.js и Alpine Linux. Обеспечивает минималистичную, быструю и безопасную среду для разработки и развертывания веб-приложений'
    },
    {
        icon: '🔷',
        header: 'TypeScript',
        content: 'Язык программирования, расширяющий JavaScript за счёт статической типизации. Улучшает качество кода, делает разработку удобнее и снижает количество ошибок'
    },
    {
        icon: '⚛️',
        header: 'React',
        content: 'Популярная библиотека для создания динамичных и отзывчивых пользовательских интерфейсов. Позволяет строить приложения на основе компонентов и эффективно управлять состоянием'
    },
    {
        icon: '🛣️',
        header: 'React Router',
        content: 'Библиотека для маршрутизации в React-приложениях. Позволяет реализовывать навигацию между страницами без перезагрузки, обеспечивая плавный пользовательский опыт'
    }
];

export const AboutPage: React.FC = () => {
    return (
        <div className="info-container">
            <h1>Используемые технологии</h1>
            <div className="info-grid">
                { info.map( (value) =>
                    <InfoCard icon={ value.icon } header={ value.header } content={ value.content } />
                ) }
            </div>
            <NavigationButton path='/' label='Назад' />
        </div>
    )
}