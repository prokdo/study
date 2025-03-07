import { JSX, useState } from 'react'

import { loginUser } from '../../api/auth.ts'
import { LoginFormProps } from '../../types/props/LoginFormProps.ts'

import ActionButton from '../ActionButton/ActionButton.tsx'

import './LoginForm.scss'

export default function LoginForm({ email, onLoginSuccess, onBack }: LoginFormProps): JSX.Element {
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async () => {
        try {
            await loginUser({ email, password });
            onLoginSuccess();
        } catch (err: any) {
            setError(err.message || 'Login failed');
        }
    };

    return (
        <div className='form-login'>
            <h2 className='header'>Login</h2>
            <p className='email'>Email: {email}</p>
            <input
                className='input-password'
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
            {error && <p className='error' style={{ color: 'red' }}>{error}</p>}
            <ActionButton label="Login" action={handleLogin} />
            <ActionButton label="Back" action={onBack} />
        </div>
    );
}