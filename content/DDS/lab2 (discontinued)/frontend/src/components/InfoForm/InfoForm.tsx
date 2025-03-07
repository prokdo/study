import React from 'react'

import { getUserByEmail } from '../../api/users.ts'
import { InfoFormProps } from '../../types/props/InfoFormProps.ts'

import ActionButton from '../ActionButton/ActionButton.tsx'

import './InfoForm.scss'

export default function InfoForm({ email, onLogout }: InfoFormProps) {
    const [name, setName] = React.useState('');

    React.useEffect(() => {
        const fetchUserData = async () => {
            try {
                const user = await getUserByEmail(email);
                setName(user.name);
            } catch (err: any) {
                console.error('Failed to fetch user data:', err.message);
            }
        };
        fetchUserData();
    }, [email]);

    return (
        <div className='form-info'>
            <h2 className='header'>Welcome, {name}!</h2>
            <ActionButton label="Next" action={() => window.location.href = '/about'} />
            <ActionButton label="Logout" action={onLogout} />
        </div>
    );
}