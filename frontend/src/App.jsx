import { useState, useEffect } from 'react'
import Header from './components/Header'
import FilterBar from './components/FilterBar'
import ArticleGrid from './components/ArticleGrid'
import './App.css'

function App() {
  const [articles, setArticles] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [selectedLevel, setSelectedLevel] = useState('')

  useEffect(() => {
    fetchArticles()
  }, [])

  const fetchArticles = async () => {
    try {
      setLoading(true)
      const response = await fetch('http://localhost:8080/api/v1/articles')
      if (!response.ok) {
        throw new Error('Failed to fetch articles')
      }
      const data = await response.json()
      setArticles(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const filteredArticles = selectedLevel
    ? articles.filter(article => article.cefrLevel === selectedLevel)
    : articles

  return (
    <div className="app-container">
      <Header />

      <main>
        <FilterBar
          selectedLevel={selectedLevel}
          onLevelChange={setSelectedLevel}
        />

        {loading && (
          <div className="loading">
            <div className="spinner"></div>
            <p>Fetching latest articles...</p>
          </div>
        )}

        {error && (
          <div className="error-message">
            <p>Error: {error}</p>
            <button onClick={fetchArticles}>Try Again</button>
          </div>
        )}

        {!loading && !error && (
          <ArticleGrid articles={filteredArticles} />
        )}
      </main>
    </div>
  )
}

export default App
