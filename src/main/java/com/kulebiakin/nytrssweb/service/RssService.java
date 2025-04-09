package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.config.CacheConfig;
import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.impl.RssConverterImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RssService {
    private final RssConverter rssConverter;

    @Value("${rss.technology.url:https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml}")
    private String technologyRssUrl;

    @Cacheable(CacheConfig.ARTICLES_CACHE)
    public List<Article> fetchTechnologyArticles() throws Exception {
        log.info("Fetching articles from RSS feed: {}", technologyRssUrl);
        try (InputStreamReader reader = new InputStreamReader(new URL(technologyRssUrl).openStream())) {
            return rssConverter.convertToArticles(reader);
        }
    }
}
