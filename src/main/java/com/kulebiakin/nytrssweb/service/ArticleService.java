package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.client.RssWebService;
import com.kulebiakin.nytrssweb.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private static final String TECHNOLOGY_CACHE = "articles";
    private final RssConverter rssConverter;
    private final RssWebService rssWebService;

    @Cacheable(TECHNOLOGY_CACHE)
    public List<Article> fetchTechnologyArticles() throws Exception {
        ByteArrayInputStream rssXmlIs = rssWebService.getTechnologyRssFeed();

        return rssConverter.convertToArticles(rssXmlIs);
    }
}
