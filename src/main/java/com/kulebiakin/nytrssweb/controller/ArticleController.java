package com.kulebiakin.nytrssweb.controller;

import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/technology")
    public ResponseEntity<List<Article>> technology() throws Exception {
        return ResponseEntity.ok(articleService.fetchTechnologyArticles());
    }
}
