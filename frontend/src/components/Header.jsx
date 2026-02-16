import React from 'react';

const Header = () => {
    return (
        <header style={styles.header}>
            <div style={styles.container}>
                <h1 style={styles.logo}>LevelReader</h1>
                <p style={styles.subtitle}>Curated News for English Learners</p>
            </div>
        </header>
    );
};

const styles = {
    header: {
        padding: '1.5rem 0',
        borderBottom: '1px solid var(--bg-card)',
        marginBottom: '2rem',
    },
    container: {
        maxWidth: 'var(--container-width)',
        margin: '0 auto',
        padding: '0 2rem',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    logo: {
        fontSize: '1.8rem',
        fontWeight: '700',
        color: 'var(--text-primary)',
        margin: 0,
    },
    subtitle: {
        color: 'var(--text-secondary)',
        margin: 0,
        fontSize: '0.9rem',
    }
};

export default Header;
