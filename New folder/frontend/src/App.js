// src/App.js

import React, { useState } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import DashboardContent from './components/DashboardContent';
import './App.css';

// Backend URL from environment or default
const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function App() {
    const [loggedInAccount, setLoggedInAccount] = useState(null);
    const [loggedInUsername, setLoggedInUsername] = useState(null);
    const [view, setView] = useState('login'); // login | register | dashboard

    // --- Authentication Handlers ---
    const handleLoginSuccess = ({ accountNumber, username }) => {
        setLoggedInAccount(accountNumber);
        setLoggedInUsername(username);
        setView('dashboard');
    };

    const handleLogout = async () => {
        try {
            await fetch(`${BACKEND_URL}/api/v1/users/logout`, { method: 'POST' });
        } catch (error) {
            console.error("Logout API call failed, clearing client session anyway:", error);
        }
        setLoggedInAccount(null);
        setLoggedInUsername(null);
        setView('login');
    };

    // --- Navigation Helpers ---
    const navigateToLogin = () => setView('login');
    const navigateToRegister = () => setView('register');

    // --- Conditional Rendering ---
    if (loggedInAccount && loggedInUsername) {
        return (
            <div className="App">
                <DashboardContent
                    accountNumber={loggedInAccount}
                    username={loggedInUsername}
                    onLogout={handleLogout}
                    backendUrl={BACKEND_URL} // optional: pass to child components if needed
                />
            </div>
        );
    }

    return (
        <div className="App auth-container">
            <header style={{ textAlign: 'center', padding: '20px', color: '#FF8C00' }}>
                <h1>Simple Online Banking</h1>
            </header>

            <main style={{ margin: '0 auto', padding: '20px' }}>
                {view === 'register' ? (
                    <Register navigateToLogin={navigateToLogin} backendUrl={BACKEND_URL} />
                ) : (
                    <Login
                        onLoginSuccess={handleLoginSuccess}
                        navigateToRegister={navigateToRegister}
                        backendUrl={BACKEND_URL}
                    />
                )}
            </main>
        </div>
    );
}

export default App;
