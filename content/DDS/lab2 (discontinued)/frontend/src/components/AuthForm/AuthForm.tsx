import { JSX, useState } from 'react'

import { AuthFormProps } from '../../types/props/AuthFormProps.ts'

import ActionButton from '../ActionButton/ActionButton.tsx'

import './AuthForm.scss'

export default function AuthForm({ onEmailSubmit }: AuthFormProps): JSX.Element {
    const [email, setEmail] = useState('');

    const handleSubmit = () => {
        if (email.trim() === '') {
            alert('Please enter your email');
            return;
        }
        onEmailSubmit(email);
    };

    return (
        <div className='form-auth'>
            <h2 className='header'>Enter your email</h2>
            <input
                className='email-input'
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Email"
            />
            <ActionButton label="Next" action={handleSubmit} />
        </div>
    );
}