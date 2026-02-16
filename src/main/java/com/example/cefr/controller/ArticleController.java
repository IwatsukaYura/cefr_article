package com.example.cefr.controller;

import com.example.cefr.model.Article;
import com.example.cefr.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public List<Article> getArticles(@RequestParam(required = false) String level) {
        if (level != null && !level.isBlank()) {
            Article probe = new Article();
            probe.setCefrLevel(level);
            // Ignore other fields, match only level
            return articleRepository.findAll(Example.of(probe));
        }
        return articleRepository.findAll();
    }
}
