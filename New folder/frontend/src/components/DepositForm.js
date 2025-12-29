import React, { useState } from 'react';

// DTO/Request structure for the backend Deposit endpoint:
// { accountId: "...", amount: 100.00 }

function DepositForm({ accountId, onTransactionSuccess }) {
    const [amount, setAmount] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError(null);

        const depositAmount = parseFloat(amount);

        // Client-side Validation
        if (!depositAmount || depositAmount <= 0) {
            setError('Please enter a valid deposit amount.');
            return;
        }

        setLoading(true);

        // Prepare the request body
        const depositRequest = {
            accountId: accountId,
            amount: depositAmount,
            // You could include a description field if needed in the future
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/transactions/deposit', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(depositRequest),
            });

            const data = await response.json();

            if (response.ok) {
                setMessage(`Deposit of $${depositAmount.toFixed(2)} successful!`);
                setAmount('');
                // Call the callback function to trigger a dashboard refresh
                onTransactionSuccess();
            } else {
                // Handles 4xx errors (e.g., Validation failed on the backend)
                setError(data.message || 'Deposit failed due to a server error.');
            }
        } catch (err) {
            // Handles network errors
            setError('Could not connect to the banking server for deposit.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <h4 style={styles.title}>💰 Deposit Money</h4>
            <form onSubmit={handleSubmit} style={styles.form}>
                <input
                    type="number"
                    step="0.01"
                    placeholder="Amount to Deposit"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    style={styles.input}
                    min="0.01"
                    disabled={loading}
                />
                
                {error && <p style={styles.error}>{error}</p>}
                {message && <p style={styles.success}>{message}</p>}

                <button type="submit" style={styles.button} disabled={loading}>
                    {loading ? 'Processing...' : 'Deposit'}
                </button>
            </form>
        </div>
    );
}

export default DepositForm;

// --- Styles (adjusted from DashboardContent for the form container) ---
const styles = {
    container: {
        padding: '20px',
        border: '1px solid #FFD6B0',
        borderRadius: '10px',
        backgroundColor: '#FFFFFF',
        boxShadow: '0 5px 15px rgba(255, 140, 0, 0.05)',
    },
    title: {
        color: '#FF8C00',
        borderBottom: '1px solid #FFD6B0',
        paddingBottom: '10px',
        marginBottom: '15px',
        fontSize: '1.2em'
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    input: {
        height: '40px',
        padding: '0 10px',
        marginBottom: '15px',
        border: '1px solid #ccc',
        borderRadius: '5px',
    },
    button: {
        padding: '10px',
        backgroundColor: '#FF8C00',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontWeight: 'bold',
    },
    error: {
        color: 'red',
        marginBottom: '10px',
        fontSize: '0.9em'
    },
    success: {
        color: 'green',
        marginBottom: '10px',
        fontSize: '0.9em'
    }
};