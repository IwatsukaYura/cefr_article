import React from 'react';
import ArticleCard from './ArticleCard';

const ArticleGrid = ({ articles }) => {
    if (!articles || articles.length === 0) {
        return (
            <div style={styles.emptyState}>
                <p>No articles found for this level.</p>
            </div>
        );
    }

    return (
        <div style={styles.grid}>
            {articles.map((article) => (
                <ArticleCard key={article.id} article={article} />
            ))}
        </div>
    );
};

const styles = {
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
        gap: '2rem',
    },
    emptyState: {
        textAlign: 'center',
        padding: '4rem',
        color: 'var(--text-secondary)',
        fontSize: '1.1rem',
    }
};

export default ArticleGrid;
