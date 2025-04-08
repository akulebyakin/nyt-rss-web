package com.kulebiakin.nytrssweb.controller;

import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.RssService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final RssService rssService;

    public ArticleController(RssService rssService) {
        this.rssService = rssService;
    }

    @GetMapping
    public ResponseEntity<List<Article>> all() throws Exception {
        return ResponseEntity.ok(rssService.fetchArticles());
    }
}
