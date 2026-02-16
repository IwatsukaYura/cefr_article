# 1. 要件定義書 (Requirements Definition)

## 1.1 システム概要

英語学習者が、自身の習熟度（CEFRレベル）に合ったニュース記事を効率的に発見するためのバックエンドAPIサービスです。外部のニュースソースから記事を自動収集し、独自のアルゴリズムで難易度を判定・分類して提供します。

## 1.2 背景と目的

- **課題:** 英語学習において「自分のレベルに合った（i+1）教材」を選ぶことは重要だが、Web上のニュースをレベル別に仕分けるのは手作業では困難。
- **解決策:** 自然言語処理（NLP）を用いて記事の難易度を数値化（CEFRレベル）し、APIを通じて学習者に最適なコンテンツを提供する。

## 1.3 機能要件

| ID | 機能名 | 内容 |
|------|------|------|
| F-01 | 記事自動収集 | 外部RSSフィード（BBC, Reuters等）から定期的に最新記事を取得する。 |
| F-02 | 本文抽出処理 | URLから記事本文を抽出し、解析可能なテキストデータに変換する。 |
| F-03 | 難易度解析 | 構文・語彙の観点からテキストを解析し、CEFRレベル（A1〜C2）を判定する。 |
| F-04 | 記事データ永続化 | 解析済みの記事（タイトル、URL、レベル、公開日）をDBに保存する。 |
| F-05 | レベル別記事一覧API | 指定されたCEFRレベルに基づき、記事一覧をJSON形式で提供する。 |

## 1.4 非機能要件

- **保守性:** Spring Bootの標準的なレイヤードアーキテクチャに従い、テストコード（JUnit）を実装する。
- **拡張性:** 将来的なユーザー認証追加や、解析エンジンのAI（OpenAI API等）への差し替えが容易な疎結合設計とする。
- **信頼性:** 外部APIのタイムアウトやフォーマット変更に対し、例外処理を行いシステム全体を停止させない。

---

# 2. 基本設計書 (Basic Design)

## 2.1 システムアーキテクチャ

Spring Boot 3.x を採用し、以下のレイヤー構造で設計します。

- **Controller:** REST APIのエンドポイント。リクエストのバリデーションを担当。
- **Service:** ビジネスロジック（RSS取得、難易度算出）の実行。
- **Repository:** Spring Data JPAを用いたデータベース操作。
- **Scheduler:** 定期的なクローリングジョブの実行。

## 2.2 データベース設計

### articles（記事テーブル）

| カラム名 | 型 | 制約 | 説明 |
|------|------|------|------|
| `id` | BIGINT | PK, Auto Increment | 内部ID |
| `title` | VARCHAR(255) | NOT NULL | 記事のタイトル |
| `url` | VARCHAR(512) | UNIQUE, NOT NULL | 重複排除のためのユニークURL |
| `cefr_level` | VARCHAR(2) | INDEX | A1, A2, B1, B2, C1, C2 |
| `readability_score` | DOUBLE | | 難易度算出の生スコア |
| `published_at` | TIMESTAMP | INDEX | ニュースの公開日時 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | 取得日時 |

## 2.3 API仕様

### 記事一覧取得

- **Endpoint:** `GET /api/v1/articles`
- **Query Parameters:**
  - `level` (Optional): フィルタリングするレベル (e.g., `B2`)
- **Response:** `200 OK`

```json
[
  {
    "id": 1,
    "title": "SpaceX launches new satellite",
    "url": "https://example.com/news/123",
    "level": "B2",
    "publishedAt": "2026-02-16T10:00:00Z"
  }
]
```

## 2.4 解析ロジック (Logic Design)

初期実装では、以下の **Flesch-Kincaid Grade Level** をベースとした指標を採用します。

$$Score = 0.39 \left( \frac{\text{total words}}{\text{total sentences}} \right) + 11.8 \left( \frac{\text{total syllables}}{\text{total words}} \right) - 15.59$$

このスコアに基づき、以下の基準でCEFRへマッピングを行います（例）：

- **Grade 0-3:** A1/A2
- **Grade 4-8:** B1/B2
- **Grade 9+:** C1/C2

## 2.5 バッチ処理設計

1. **Trigger:** 1時間おきに実行 (`@Scheduled`)
2. **Fetch:** 指定したRSS URLから最新のXMLを取得。
3. **Filter:** `articles`テーブルにURLが存在しないもののみを対象とする。
4. **Process:** 記事詳細をフェッチし、難易度を計算。
5. **Save:** 解析結果をDBに永続化。