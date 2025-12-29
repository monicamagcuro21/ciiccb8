import React, { useState, useEffect } from 'react';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function UserProfile({ username }) {
    const [userDetails, setUserDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!username) {
            setError('Error: No username provided.');
            setLoading(false);
            return;
        }

        const fetchUserDetails = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`${BACKEND_URL}/api/v1/users/details/${username}`);
                
                if (response.ok) {
                    const data = await response.json();
                    setUserDetails(data);
                } else {
                    const errorData = await response.json();
                    setError(errorData.message || 'Failed to retrieve user details.');
                    setUserDetails(null);
                }
            } catch (err) {
                setError('Could not connect to the server to fetch profile details.');
                console.error('Fetch profile error:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchUserDetails();
    }, [username]);

    if (loading) return <div style={styles.container}><p>Loading user profile...</p></div>;
    if (error) return <div style={{ ...styles.container, color: 'red' }}><p>Error: {error}</p></div>;
    if (!userDetails) return <div style={styles.container}><p>No profile data available.</p></div>;

    const getFullName = () => {
        const parts = [
            userDetails.firstName,
            userDetails.middleName,
            userDetails.lastName,
            userDetails.suffix
        ].filter(Boolean);
        return parts.join(' ');
    };

    return (
        <div style={styles.container}>
            <h3 style={styles.title}>👤 Account and Personal Details</h3>

            <div style={styles.grid}>
                <div style={styles.section}>
                    <h4 style={styles.sectionTitle}>Account</h4>
                    <p><strong>Username:</strong> {userDetails.username}</p>
                    <p><strong>Account ID:</strong> {userDetails.accountNumber}</p>
                </div>

                <div style={styles.section}>
                    <h4 style={styles.sectionTitle}>Identity</h4>
                    <p><strong>Full Name:</strong> {getFullName()}</p>
                    <p><strong>Birthdate:</strong> {userDetails.birthdate}</p>
                </div>
            </div>

            <div style={styles.addressSection}>
                <h4 style={styles.sectionTitle}>Address</h4>
                <p>{userDetails.houseNoStreet}</p>
                <p>{userDetails.district}</p>
                <p>{userDetails.cityMunicipality}, {userDetails.state}</p>
            </div>
        </div>
    );
}

export default UserProfile;

const styles = {
    container: {
        padding: '30px',
        border: '1px solid #FFD6B0',
        borderRadius: '10px',
        backgroundColor: '#FFFFFF',
        boxShadow: '0 5px 15px rgba(255, 140, 0, 0.05)',
    },
    title: {
        color: '#FF8C00',
        borderBottom: '2px solid #FFD6B0',
        paddingBottom: '10px',
        marginBottom: '20px',
        fontSize: '1.5em'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '20px',
        marginBottom: '20px'
    },
    section: {
        padding: '15px',
        border: '1px solid #eee',
        borderRadius: '8px',
        backgroundColor: '#f9f9f9',
    },
    addressSection: {
        padding: '15px',
        border: '1px solid #eee',
        borderRadius: '8px',
        backgroundColor: '#f9f9f9',
    },
    sectionTitle: {
        color: '#4B2E0F',
        marginBottom: '10px',
        fontSize: '1.2em',
        fontWeight: 'bold',
    },
};
