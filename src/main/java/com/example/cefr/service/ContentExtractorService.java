package com.example.cefr.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContentExtractorService {

    public String extractContent(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // Heuristic: try to find <article> tag first
            Element article = doc.selectFirst("article");
            if (article != null) {
                return article.text();
            }

            // Fallback: try common main content classes
            Element main = doc.selectFirst("main, .main, #main, .content, #content, .post, #post");
            if (main != null) {
                return main.text();
            }

            // Last resort: body text, might include nav/footer noise
            return doc.body().text();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract content from " + url, e);
        }
    }
}
