package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RssService {
    private final RssConverter rssConverter;
    private final WebClient rssWebClient;

    @Value("${nytimes.rss.technology:Technology.xml}")
    private String technologyXmlFile;

    @Cacheable("articles")
    public List<Article> fetchTechnologyArticles() throws Exception {
        log.info("Fetching articles from RSS feed: {}", technologyXmlFile);
        String rssXml = rssWebClient.get()
                .uri("/" + technologyXmlFile)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return rssConverter.convertToArticles(rssXml);
    }
}
