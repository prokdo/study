import { JSX, useState } from 'react'

import { registerUser } from '../../api/auth.ts'
import { RegistrationFormProps } from '../../types/props/RegistrationFormProps.ts'

import ActionButton from '../ActionButton/ActionButton.tsx'

export default function RegistrationForm({ email, onRegisterSuccess, onBack }: RegistrationFormProps): JSX.Element {
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleRegister = async () => {
        try {
            await registerUser({ name: email.split('@')[0], email, password });
            onRegisterSuccess();
        } catch (err: any) {
            setError(err.message || 'Registration failed');
        }
    };

    return (
        <div className='form-registration'>
            <h2 className='header'>Register</h2>
            <p className='email'>Email: {email}</p>
            <input
                className='input-password'
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
            {error && <p className='error' style={{ color: 'red' }}>{error}</p>}
            <ActionButton label="Register" action={handleRegister} />
            <ActionButton label="Back" action={onBack} />
        </div>
    );
}