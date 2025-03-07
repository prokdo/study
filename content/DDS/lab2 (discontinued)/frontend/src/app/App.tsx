import { JSX } from 'react'

import { BrowserRouter as Router, Routes, Route } from 'react-router'

import AboutPage from '../pages/AboutPage/AboutPage.tsx'
import StartPage from '../pages/StartPage/StartPage.tsx'

import './App.scss'

export default function App(): JSX.Element {
    return (
        <Router>
            <Routes>
                <Route path='/' element={ <StartPage /> } />
                <Route path='/about' element={ <AboutPage /> } />
            </Routes>
        </Router>
    )
}