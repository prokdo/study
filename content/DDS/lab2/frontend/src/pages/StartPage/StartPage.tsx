import { JSX, useState } from 'react'

import { checkUserExistsByEmail } from '../../api/users.ts'

import AuthForm from '../../components/AuthForm/AuthForm.tsx'
import LoginForm from '../../components/LoginForm/LoginForm.tsx'
import RegistrationForm from '../../components/RegistrationForm/RegistrationForm.tsx'
import InfoForm from '../../components/InfoForm/InfoForm.tsx'

import './StartPage.scss'

export default function StartPage(): JSX.Element {
    const [step, setStep] = useState<'auth' | 'login' | 'register' | 'info'>('auth');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    const handleEmailSubmit = async (submittedEmail: string) => {
        try {
            await checkUserExistsByEmail(submittedEmail);
            setEmail(submittedEmail);
            setStep('login');
        } catch (err: any) {
            if (err.message.includes('not found')) {
                setEmail(submittedEmail);
                setStep('register');
            } else {
                setError('An error occurred while checking the email.');
            }
        }
    };

    const handleLoginSuccess = () => {
        setStep('info');
    };

    const handleRegisterSuccess = () => {
        setStep('auth');
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        setStep('auth');
    };

    return (
        <div className='page-start'>
            {error && <p className='error' style={{ color: 'red' }}>{error}</p>}
            {step === 'auth' && <AuthForm onEmailSubmit={handleEmailSubmit} />}
            {step === 'login' && (
                <LoginForm email={email} onLoginSuccess={handleLoginSuccess} onBack={() => setStep('auth')} />
            )}
            {step === 'register' && (
                <RegistrationForm email={email} onRegisterSuccess={handleRegisterSuccess} onBack={() => setStep('auth')} />
            )}
            {step === 'info' && <InfoForm email={email} onLogout={handleLogout} />}
        </div>
    );
}