// src/components/Register.js
import React, { useState } from 'react';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function Register({ navigateToLogin }) {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        birthdate: '',
        houseNoStreet: '',
        district: '',
        cityMunicipality: '',
        state: ''
    });

    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.id]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');

        const requiredFields = ['username','password','firstName','lastName','birthdate','houseNoStreet','district','cityMunicipality','state'];
        for (const field of requiredFields) {
            if (!formData[field]) {
                setError('Please fill all required fields.');
                return;
            }
        }

        try {
            const response = await fetch(`${BACKEND_URL}/api/v1/users/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                setMessage('Registration successful! Redirecting to login...');
                setTimeout(navigateToLogin, 2000);
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Registration failed.');
            }
        } catch {
            setError('Could not connect to the server.');
        }
    };

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h2 style={styles.title}>Create Account</h2>
                <form onSubmit={handleSubmit}>
                    <input id="username" placeholder="Username*" value={formData.username} onChange={handleChange} style={styles.input}/>
                    <input id="password" type="password" placeholder="Password*" value={formData.password} onChange={handleChange} style={styles.input}/>
                    <input id="firstName" placeholder="First Name*" value={formData.firstName} onChange={handleChange} style={styles.input}/>
                    <input id="lastName" placeholder="Last Name*" value={formData.lastName} onChange={handleChange} style={styles.input}/>
                    <input id="birthdate" type="date" value={formData.birthdate} onChange={handleChange} style={styles.input}/>
                    <input id="houseNoStreet" placeholder="House No. & Street*" value={formData.houseNoStreet} onChange={handleChange} style={styles.input}/>
                    <input id="district" placeholder="District*" value={formData.district} onChange={handleChange} style={styles.input}/>
                    <input id="cityMunicipality" placeholder="City / Municipality*" value={formData.cityMunicipality} onChange={handleChange} style={styles.input}/>
                    <input id="state" placeholder="State*" value={formData.state} onChange={handleChange} style={styles.input}/>

                    {message && <p style={styles.success}>{message}</p>}
                    {error && <p style={styles.error}>{error}</p>}
                    <button type="submit" style={styles.button}>Register</button>
                </form>
                <p style={styles.footer}>
                    Already have an account? <span style={styles.link} onClick={navigateToLogin}>Login</span>
                </p>
            </div>
        </div>
    );
}

export default Register;

const styles = {
    page: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: '#FFF6ED', padding: '20px' },
    card: { background: '#fff', padding: '40px', borderRadius: '18px', boxShadow: '0 20px 45px rgba(255,140,0,0.2)', width: '400px' },
    title: { fontSize: '28px', marginBottom: '20px', color: '#FF8C00', textAlign: 'center' },
    input: { width: '100%', padding: '12px', margin: '10px 0', borderRadius: '10px', border: '1px solid #FFD6B0', fontSize: '15px' },
    button: { width: '100%', padding: '12px', backgroundColor: '#FF8C00', color: '#fff', border: 'none', borderRadius: '10px', cursor: 'pointer', fontWeight: '600', marginTop: '10px' },
    error: { color: 'red', marginTop: '10px' },
    success: { color: 'green', marginTop: '10px' },
    footer: { marginTop: '15px', textAlign: 'center' },
    link: { color: '#FF8C00', fontWeight: '600', cursor: 'pointer' },
};
