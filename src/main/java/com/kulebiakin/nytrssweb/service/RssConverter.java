package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.model.Article;

import java.io.InputStreamReader;
import java.util.List;

public interface RssConverter {
    List<Article> convertToArticles(InputStreamReader reader) throws Exception;
}
