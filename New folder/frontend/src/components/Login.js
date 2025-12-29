// src/components/Login.js
import React, { useState } from 'react';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function Login({ onLoginSuccess, navigateToRegister }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!username || !password) {
            setError('Please enter username and password.');
            return;
        }

        try {
            const response = await fetch(`${BACKEND_URL}/api/v1/users/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();
            if (response.ok) {
                onLoginSuccess({ username: data.username, accountNumber: data.accountNumber });
            } else {
                setError(data.message || 'Invalid username or password.');
            }
        } catch (err) {
            setError('Could not connect to the server.');
            console.error(err);
        }
    };

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h2 style={styles.title}>Sign In</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        style={styles.input}
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        style={styles.input}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {error && <p style={styles.error}>{error}</p>}
                    <button style={styles.button} type="submit">Login</button>
                </form>
                <p style={styles.footer}>
                    New here? <span style={styles.link} onClick={navigateToRegister}>Register</span>
                </p>
            </div>
        </div>
    );
}

export default Login;

const styles = {
    page: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#FFF6ED' },
    card: { background: '#fff', padding: '40px', borderRadius: '18px', boxShadow: '0 20px 45px rgba(255,140,0,0.2)', width: '350px' },
    title: { fontSize: '28px', marginBottom: '20px', color: '#FF8C00', textAlign: 'center' },
    input: { width: '100%', padding: '12px', margin: '10px 0', borderRadius: '10px', border: '1px solid #FFD6B0', fontSize: '15px' },
    button: { width: '100%', padding: '12px', backgroundColor: '#FF8C00', color: '#fff', border: 'none', borderRadius: '10px', cursor: 'pointer', fontWeight: '600', marginTop: '10px' },
    error: { color: 'red', marginTop: '10px' },
    footer: { marginTop: '15px', textAlign: 'center' },
    link: { color: '#FF8C00', fontWeight: '600', cursor: 'pointer' },
};
