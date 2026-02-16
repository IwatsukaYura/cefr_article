import React from 'react';

const ArticleCard = ({ article }) => {
    const getBadgeColor = (level) => {
        const colors = {
            'A1': 'var(--cefr-a1)',
            'A2': 'var(--cefr-a2)',
            'B1': 'var(--cefr-b1)',
            'B2': 'var(--cefr-b2)',
            'C1': 'var(--cefr-c1)',
            'C2': 'var(--cefr-c2)',
        };
        return colors[level] || 'var(--text-secondary)';
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        return new Date(dateString).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric'
        });
    };

    return (
        <div style={styles.card}>
            <div style={styles.header}>
                <span
                    style={{
                        ...styles.badge,
                        backgroundColor: getBadgeColor(article.cefrLevel),
                        color: '#1e293b' // Dark text for contrast on bright badges
                    }}
                >
                    {article.cefrLevel || 'N/A'}
                </span>
                <span style={styles.date}>{formatDate(article.publishedAt)}</span>
            </div>

            <h3 style={styles.title}>
                <a href={article.url} target="_blank" rel="noopener noreferrer">
                    {article.title}
                </a>
            </h3>

            <div style={styles.footer}>
                <div style={styles.stat}>
                    <span style={styles.statLabel}>Readability</span>
                    <span style={styles.statValue}>{article.readabilityScore?.toFixed(1) || '-'}</span>
                </div>
                <a href={article.url} target="_blank" rel="noopener noreferrer" style={styles.link}>
                    Read Article &rarr;
                </a>
            </div>
        </div>
    );
};

const styles = {
    card: {
        backgroundColor: 'var(--bg-card)',
        borderRadius: '12px',
        padding: '1.5rem',
        display: 'flex',
        flexDirection: 'column',
        transition: 'transform 0.2s, box-shadow 0.2s',
        border: '1px solid rgba(255, 255, 255, 0.05)',
    },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '1rem',
    },
    badge: {
        padding: '0.25rem 0.75rem',
        borderRadius: '12px',
        fontSize: '0.8rem',
        fontWeight: '700',
        textTransform: 'uppercase',
    },
    date: {
        color: 'var(--text-secondary)',
        fontSize: '0.85rem',
    },
    title: {
        fontSize: '1.2rem',
        marginBottom: '1.5rem',
        lineHeight: '1.4',
        flex: 1, // Pushes footer down
    },
    footer: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginTop: 'auto',
        borderTop: '1px solid rgba(255, 255, 255, 0.1)',
        paddingTop: '1rem',
    },
    stat: {
        display: 'flex',
        flexDirection: 'column',
    },
    statLabel: {
        fontSize: '0.7rem',
        color: 'var(--text-secondary)',
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
    },
    statValue: {
        fontSize: '1rem',
        fontWeight: '600',
    },
    link: {
        fontSize: '0.9rem',
        fontWeight: '500',
    }
};

export default ArticleCard;
