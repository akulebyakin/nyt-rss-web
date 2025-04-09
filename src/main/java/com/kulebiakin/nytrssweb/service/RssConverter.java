package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.model.Article;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface RssConverter {
    List<Article> convertToArticles(ByteArrayInputStream xmlIs) throws Exception;
}
