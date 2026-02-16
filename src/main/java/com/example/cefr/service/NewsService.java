package com.example.cefr.service;

import com.example.cefr.model.Article;
import com.example.cefr.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    
    // Default feeds
    private static final List<String> RSS_FEEDS = List.of(
        "http://feeds.bbci.co.uk/news/world/rss.xml",
        "https://learningenglish.voanews.com/api/zg$omevviq"
    );

    private final RssFetcherService rssFetcherService;
    private final ContentExtractorService contentExtractorService;
    private final ReadabilityService readabilityService;
    private final ArticleRepository articleRepository;

    @Autowired
    public NewsService(
            RssFetcherService rssFetcherService,
            ContentExtractorService contentExtractorService,
            ReadabilityService readabilityService,
            ArticleRepository articleRepository) {
        this.rssFetcherService = rssFetcherService;
        this.contentExtractorService = contentExtractorService;
        this.readabilityService = readabilityService;
        this.articleRepository = articleRepository;
    }

    @Scheduled(fixedRate = 3600000) // 1 hour
    public void fetchAndProcessArticles() {
        logger.info("Starting scheduled article fetch at {}", LocalDateTime.now());
        
        for (String rssUrl : RSS_FEEDS) {
            processFeed(rssUrl);
        }
        
        logger.info("Completed scheduled article fetch.");
    }

    @Transactional
    public void processFeed(String rssUrl) {
        try {
            List<Article> fetchedArticles = rssFetcherService.fetchArticles(rssUrl);
            
            for (Article article : fetchedArticles) {
                if (articleRepository.findByUrl(article.getUrl()).isPresent()) {
                    continue; // Skip existing
                }

                try {
                    String content = contentExtractorService.extractContent(article.getUrl());
                    double score = readabilityService.calculateFleschKincaidGradeLevel(content);
                    String cefr = readabilityService.calculateCefrLevel(content);

                    article.setReadabilityScore(score);
                    article.setCefrLevel(cefr);
                    
                    articleRepository.save(article);
                    logger.info("Saved article: {} [CEFR: {}]", article.getTitle(), cefr);
                    
                    // Be polite to servers
                    Thread.sleep(1000); 
                } catch (Exception e) {
                    logger.error("Error processing article: {}", article.getUrl(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching feed: {}", rssUrl, e);
        }
    }
}
