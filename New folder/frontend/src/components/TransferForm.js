// src/components/TransferForm.js
import React, { useState } from 'react';

function TransferForm({ accountId, onTransactionSuccess }) {
    const [toAccountId, setToAccountId] = useState('');
    const [amount, setAmount] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError(null);

        const transferAmount = parseFloat(amount);
        if (isNaN(transferAmount) || transferAmount <= 0) {
            setError('Please enter a valid amount greater than $0.00.');
            return;
        }
        if (toAccountId === accountId) {
            setError('Cannot transfer funds to your own account.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/transactions/transfer', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ 
                    fromAccountId: accountId, 
                    toAccountId: toAccountId, 
                    amount: transferAmount 
                })
            });

            const data = await response.json();

            if (response.ok) {
                setMessage(`Transfer successful! Sender's new balance: $${parseFloat(data.newBalance).toFixed(2)}`);
                setError(null);
                setToAccountId('');
                setAmount('');
                
                if (onTransactionSuccess) {
                    onTransactionSuccess();
                }

            } else {
                setError(data.message || 'Transfer failed due to a server error.');
                setMessage('');
            }

        } catch (err) {
            setError('Could not connect to the banking server.');
            setMessage('');
        }
    };
    
    // --- STYLES (Reusing the theme) ---
    const inputStyle = {
        padding: '12px 10px',
        fontSize: '16px',
        border: '1px solid #FFD6B0',
        borderRadius: '5px',
        width: '100%',
        boxSizing: 'border-box',
        marginBottom: '15px'
    };

    const buttonStyle = {
        padding: '12px 20px',
        backgroundColor: '#FF8C00',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '16px',
        fontWeight: 'bold',
        width: '100%'
    };

    return (
        <div style={{ marginBottom: '30px', padding: '20px', border: '1px solid #f0f0f0', borderRadius: '8px' }}>
            <h3 style={{ color: '#FF8C00', borderBottom: '2px solid #FFD6B0', paddingBottom: '10px', marginBottom: '20px' }}>
                Transfer Funds
            </h3>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="toAccountId" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Recipient Account ID:</label>
                    <input
                        type="text"
                        id="toAccountId"
                        value={toAccountId}
                        onChange={(e) => setToAccountId(e.target.value)}
                        placeholder="Enter recipient's account number (e.g., NB000...)"
                        required
                        style={inputStyle}
                    />
                </div>
                
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="amount" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Amount ($):</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="Enter amount to transfer"
                        required
                        min="0.01"
                        step="0.01"
                        style={inputStyle}
                    />
                </div>
                
                {message && <p style={{ color: 'green', fontWeight: 'bold' }}>{message}</p>}
                {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}

                <button type="submit" style={buttonStyle}>
                    Transfer
                </button>
            </form>
        </div>
    );
}

export default TransferForm;