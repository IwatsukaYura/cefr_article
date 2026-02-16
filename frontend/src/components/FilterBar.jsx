import React from 'react';

const FilterBar = ({ selectedLevel, onLevelChange }) => {
    const levels = ['All', 'A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

    return (
        <div style={styles.container}>
            <span style={styles.label}>Filter by Level:</span>
            <div style={styles.buttonGroup}>
                {levels.map((level) => (
                    <button
                        key={level}
                        onClick={() => onLevelChange(level === 'All' ? '' : level)}
                        style={{
                            ...styles.button,
                            ...(selectedLevel === (level === 'All' ? '' : level) ? styles.activeButton : {})
                        }}
                    >
                        {level}
                    </button>
                ))}
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        alignItems: 'center',
        marginBottom: '2rem',
        gap: '1rem',
        flexWrap: 'wrap',
    },
    label: {
        color: 'var(--text-secondary)',
        fontWeight: '500',
    },
    buttonGroup: {
        display: 'flex',
        gap: '0.5rem',
        flexWrap: 'wrap',
    },
    button: {
        padding: '0.5rem 1rem',
        borderRadius: '20px',
        border: '1px solid var(--bg-card)',
        color: 'var(--text-secondary)',
        transition: 'all 0.2s',
        fontSize: '0.9rem',
    },
    activeButton: {
        backgroundColor: 'var(--accent-primary)',
        color: '#fff',
        borderColor: 'var(--accent-primary)',
        boxShadow: '0 0 10px rgba(56, 189, 248, 0.3)',
    }
};

export default FilterBar;
