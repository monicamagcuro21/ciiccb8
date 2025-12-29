import React, { useState, useEffect } from 'react';

function TransactionHistory({ accountId }) {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null); 

    useEffect(() => {
        // The URL is correctly mapped to your updated TransactionController
        fetch(`http://localhost:8080/api/v1/transactions?accountId=${accountId}`)
          .then(response => {
            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json(); 
          })
          .then(data => {
            setTransactions(data); 
            setLoading(false);
          })
          .catch(err => {
            console.error('Fetch error:', err);
            setError(err.message); 
            setLoading(false);
          });
    }, [accountId]);

    if (loading) {
        return <div>Loading transactions...</div>;
    }
    
    if (error) {
        return <div style={{ color: 'red' }}>Error loading transactions: {error}. Check your Spring Boot server.</div>;
    }

    // Function to format the timestamp array [year, month, day, hour, minute, second]
    // If your Java object uses a simple ISO String (e.g., "2025-12-15T10:30:00"), you can simplify this back to {new Date(tx.timestamp).toLocaleString()}
    const formatTimestamp = (timestampArray) => {
        if (!timestampArray || timestampArray.length < 3) return 'N/A';
        // Constructs a JS Date object from the Java array [year, month, day, hour, minute, second]
        // NOTE: Month in JS Date is 0-indexed, so we use timestampArray[1] - 1
        const date = new Date(
            timestampArray[0], 
            timestampArray[1] - 1, 
            timestampArray[2], 
            timestampArray[3] || 0, // Hour
            timestampArray[4] || 0, // Minute
            timestampArray[5] || 0  // Second
        );
        return date.toLocaleString();
    };

    // Render the data in a table
    return (
        <div>
            <h2>Transaction History for Account #{accountId}</h2>
            {transactions.length === 0 ? (
                <p>No transactions found for this account.</p>
            ) : (
                <table>
                    <thead>
                      <tr><th>Date/Time</th><th>Description</th><th>Amount</th><th>Type</th></tr>
                    </thead>
                    <tbody>
                      {transactions.map(tx => (
                        <tr key={tx.id}>
                          {/* 🛑 UPDATED: Use formatTimestamp on the 'timestamp' property */}
                          <td>{formatTimestamp(tx.timestamp)}</td> 
                          <td>{tx.description}</td>
                          <td>
                            {new Intl.NumberFormat('en-US', {
                                style: 'currency',
                                currency: 'USD', 
                                minimumFractionDigits: 2,
                            }).format(tx.amount)}
                          </td>
                          <td>{tx.type}</td>
                        </tr>
                      ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default TransactionHistory;