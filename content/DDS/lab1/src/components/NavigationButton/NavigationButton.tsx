import React from 'react';

import { NavigateFunction, useNavigate } from 'react-router';

import './NavigationButton.css';

interface NavigationButtonProps {
    path: string
    label: string;
}

const NavigationButton: React.FC<NavigationButtonProps> = ( { path, label } ) => {
    const navigate: NavigateFunction = useNavigate();

    return (
        <button className='nav-button' onClick={ () => navigate(path) }> { label } </button>
    )
}

export default NavigationButton;