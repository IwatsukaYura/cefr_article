# CEFR Level News Reader

A full-stack application that fetches news articles from RSS feeds, analyzes their English difficulty level (CEFR: A1-C2) using readability algorithms, and displays them in a modern, dark-mode React interface.

## 🚀 Features

*   **Automated Content Aggregation**: Fetches articles hourly from diverse sources:
    *   **BBC News** (Standard/Advanced English)
    *   **Breaking News English** (Simpler English for learners)
*   **CEFR Classification**: Automatically calculates the CEFR level (A1-C2) for each article based on Flesch-Kincaid readability scores.
*   **Modern Frontend**: A responsive, premium-styled React application with dark mode by default.
*   **Filtering**: Easily filter articles by difficulty level (e.g., "Show only B2 articles").

## 🛠️ Tech Stack

### Backend
*   **Java 21**
*   **Spring Boot 3.2**
*   **Gradle** (Kotlin DSL)
*   **Jsoup** (HTML Parsing)
*   **ROME** (RSS Parsing)

### Frontend
*   **React 18**
*   **Vite**
*   **Vanilla CSS** (Variables & Modern Layouts)

### Infrastructure
*   **PostgreSQL 15**
*   **Docker & Docker Compose**

## 📋 Prerequisites

*   **Docker Desktop** (for backend & database)
*   **Node.js 20+** (only if running frontend locally without Docker)

## 🏁 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/IwatsukaYura/cefr_article.git
cd cefr_article
```

### 2. Start Backend & Database
Use Docker Compose to build and start the Spring Boot application and PostgreSQL database.

```bash
docker compose up --build -d
```

*   **API URL**: `http://localhost:8080/api/v1/articles`
*   **Database Port**: `5434` (mapped to host)

### 3. Start Frontend
Open a new terminal window and navigate to the frontend directory.

```bash
cd frontend
npm install
npm run dev
```

*   **Frontend URL**: `http://localhost:5173`

## 📚 API Endpoints

### Get Articles
Fetch all processed articles.

`GET /api/v1/articles`

**Response:**
```json
[
  {
    "id": 1,
    "title": "Example News Article",
    "url": "https://...",
    "cefrLevel": "B2",
    "readabilityScore": 8.5,
    "publishedAt": "2026-02-16T10:00:00"
  }
]
```

### Filter by Level
Fetch articles for a specific CEFR level.

`GET /api/v1/articles?level=C1`

## 🗄️ Project Structure

```
├── src/main/java       # Spring Boot Backend source code
├── frontend/           # React Frontend source code
├── docker-compose.yml  # Container orchestration
└── Dockerfile          # Backend container definition
```
