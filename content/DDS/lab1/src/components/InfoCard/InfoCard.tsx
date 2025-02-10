import React from 'react';

import './InfoCard.css';

interface InfoCardProps {
    icon: string,
    header: string,
    content: string
}

const InfoCard: React.FC<InfoCardProps> = ( { icon, header, content } ) => {
    return (
        <div className='info-card'>
            <div className='icon'>{ icon }</div>
            <h2>{ header }</h2>
            <p>{ content }</p>
        </div>
    )
}

export default InfoCard;