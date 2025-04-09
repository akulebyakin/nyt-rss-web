package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.model.Article;

import java.util.List;

public interface RssConverter {
    List<Article> convertToArticles(String xml) throws Exception;
}
