// src/components/WithdrawForm.js
import React, { useState } from 'react';

function WithdrawForm({ accountId, onTransactionSuccess }) {
    const [amount, setAmount] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError(null);

        const withdrawAmount = parseFloat(amount);
        if (isNaN(withdrawAmount) || withdrawAmount <= 0) {
            setError('Please enter a valid amount greater than $0.00.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/transactions/withdraw', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ 
                    accountId: accountId, 
                    amount: withdrawAmount 
                })
            });

            const data = await response.json();

            if (response.ok) {
                setMessage(`Withdrawal successful! New balance: $${parseFloat(data.newBalance).toFixed(2)}`);
                setError(null);
                setAmount('');
                
                if (onTransactionSuccess) {
                    onTransactionSuccess();
                }

            } else {
                setError(data.message || 'Withdrawal failed due to a server error.');
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
                Withdraw Funds
            </h3>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="amount" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Amount ($):</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="Enter amount to withdraw"
                        required
                        min="0.01"
                        step="0.01"
                        style={inputStyle}
                    />
                </div>
                
                {message && <p style={{ color: 'green', fontWeight: 'bold' }}>{message}</p>}
                {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}

                <button type="submit" style={buttonStyle}>
                    Withdraw
                </button>
            </form>
        </div>
    );
}

export default WithdrawForm;