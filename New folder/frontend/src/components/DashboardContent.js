// src/components/DashboardContent.js

import React, { useState, useEffect } from 'react';
import DepositForm from './DepositForm'; 
import WithdrawForm from './WithdrawForm'; 
import TransferForm from './TransferForm'; 
import UserProfile from './UserProfile'; 

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function DashboardContent({ accountNumber, username, onLogout }) {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [balance, setBalance] = useState('0.00'); 
    const [refreshKey, setRefreshKey] = useState(0); 
    const [activeView, setActiveView] = useState('transactions'); // 'transactions' or 'profile'

    // Callback to refresh data after successful transaction
    const handleTransactionSuccess = () => {
        setRefreshKey(prevKey => prevKey + 1);
        setActiveView('transactions');
    };

    // Format currency
    const formatMoney = (amount) => {
        const numAmount = parseFloat(amount);
        return `$${numAmount.toFixed(2)}`;
    };

    // Fetch balance and transaction history
    useEffect(() => {
        if (!accountNumber) return;

        const fetchDashboardData = async () => {
            setLoading(true);
            setError(null);

            try {
                // Fetch Balance
                const balanceResponse = await fetch(`${BACKEND_URL}/api/v1/transactions/balance/${accountNumber}`);
                if (balanceResponse.ok) {
                    const data = await balanceResponse.json();
                    setBalance(data.balance);
                } else {
                    console.error('Failed to fetch balance.');
                    setBalance('---');
                }

                // Fetch Transaction History only if viewing transactions
                if (activeView === 'transactions') {
                    const historyResponse = await fetch(`${BACKEND_URL}/api/v1/transactions/history/${accountNumber}`);
                    if (historyResponse.ok) {
                        const data = await historyResponse.json();
                        setTransactions(data);
                    } else {
                        setError('Failed to fetch transaction history.');
                        setTransactions([]);
                    }
                }
            } catch (err) {
                setError('Could not connect to server to fetch dashboard data.');
                console.error('Fetch dashboard error:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, [accountNumber, refreshKey, activeView]);

    // Navigation tab styles
    const navButtonStyle = (view) => ({
        padding: '10px 20px',
        marginRight: '10px',
        backgroundColor: activeView === view ? '#FF8C00' : '#f0f0f0',
        color: activeView === view ? 'white' : '#444',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontWeight: 'bold',
        transition: 'background-color 0.2s'
    });

    // Main content renderer
    const renderContent = () => {
        if (activeView === 'profile') {
            return <UserProfile username={username} />;
        }

        return (
            <>
                {/* Transaction Forms */}
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '20px' }}>
                    <DepositForm accountNumber={accountNumber} onTransactionSuccess={handleTransactionSuccess} />
                    <WithdrawForm accountNumber={accountNumber} onTransactionSuccess={handleTransactionSuccess} />
                    <TransferForm accountNumber={accountNumber} onTransactionSuccess={handleTransactionSuccess} />
                </div>

                {/* Transaction History */}
                <h3 style={{ color: '#FF8C00', borderBottom: '2px solid #FFD6B0', paddingBottom: '10px', marginBottom: '20px', marginTop: '40px' }}>
                    Transaction History
                </h3>

                {loading && <p>Loading history...</p>}
                {error && <p style={{ color: 'red' }}>Error: {error}</p>}

                {!loading && transactions.length > 0 ? (
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '15px', fontSize: '0.95em' }}>
                        <thead>
                            <tr style={{ backgroundColor: '#FFD6B0' }}>
                                <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Date</th>
                                <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Type</th>
                                <th style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>Amount</th>
                                <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Details</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map(tx => (
                                <tr key={tx.transactionId || tx.receiptNumber} style={{ borderBottom: '1px solid #eee' }}>
                                    <td style={{ padding: '10px', border: '1px solid #eee' }}>{new Date(tx.timestamp).toLocaleString()}</td>
                                    <td style={{ padding: '10px', border: '1px solid #eee', fontWeight: 'bold', 
                                        color: tx.transactionType?.includes('DEPOSIT') || tx.transactionType?.includes('RECEIVED') ? 'green' : 'red' }}>
                                        {tx.transactionType}
                                    </td>
                                    <td style={{ padding: '10px', border: '1px solid #eee', textAlign: 'right', fontWeight: 'bold' }}>
                                        {formatMoney(tx.amount)}
                                    </td>
                                    <td style={{ padding: '10px', border: '1px solid #eee' }}>{tx.description || tx.relatedAccount || 'N/A'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    !loading && <p>No transactions found.</p>
                )}
            </>
        );
    };

    return (
        <div style={{ padding: '20px 40px', maxWidth: '1200px', margin: '0 auto' }}>
            {/* Header and Logout */}
            <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
                <h1 style={{ color: '#FF8C00' }}>Welcome, {username}!</h1>
                <button 
                    onClick={onLogout} 
                    style={{ padding: '10px 20px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }}>
                    Logout
                </button>
            </header>

            {/* Balance Card */}
            <div style={{ backgroundColor: '#FF8C00', color: 'white', padding: '25px', borderRadius: '10px', marginBottom: '30px' }}>
                <h2 style={{ margin: '0 0 5px 0', fontSize: '1.2em' }}>Current Balance</h2>
                <h1 style={{ margin: '0', fontSize: '3em' }}>{formatMoney(balance)}</h1>
                <p style={{ margin: '5px 0 0 0', opacity: 0.8 }}>Account Number: {accountNumber}</p>
            </div>

            {/* Navigation Tabs */}
            <div style={{ marginBottom: '25px', borderBottom: '2px solid #f0f0f0' }}>
                <button style={navButtonStyle('transactions')} onClick={() => setActiveView('transactions')}>
                    Transactions & History
                </button>
                <button style={navButtonStyle('profile')} onClick={() => setActiveView('profile')}>
                    My Profile Details
                </button>
            </div>

            {/* Main Content */}
            {renderContent()}
        </div>
    );
}

export default DashboardContent;
