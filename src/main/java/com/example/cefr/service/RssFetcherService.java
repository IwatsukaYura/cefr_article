package com.example.cefr.service;

import com.example.cefr.model.Article;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RssFetcherService {

    public List<Article> fetchArticles(String rssUrl) {
        List<Article> articles = new ArrayList<>();
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                    .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                    .build();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(rssUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .build();

            java.net.http.HttpResponse<java.io.InputStream> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch RSS: " + response.statusCode());
            }

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(response.body()));

            for (SyndEntry entry : feed.getEntries()) {
                Article article = new Article();
                article.setTitle(entry.getTitle());
                article.setUrl(entry.getLink());
                
                Date publishedDate = entry.getPublishedDate();
                if (publishedDate != null) {
                    article.setPublishedAt(publishedDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                }
                
                articles.add(article);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch RSS from " + rssUrl, e);
        }
        return articles;
    }
}
