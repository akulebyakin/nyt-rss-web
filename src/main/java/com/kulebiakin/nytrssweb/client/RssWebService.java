package com.kulebiakin.nytrssweb.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.util.Objects;

@Slf4j
@Service
public class RssWebService {
    private final WebClient rssWebClient;
    private final String technologyFeedName;

    public RssWebService(WebClient rssWebClient, @Value("${nytimes.rss.technology:Technology.xml}") String technologyFeedName) {
        this.rssWebClient = rssWebClient;
        this.technologyFeedName = technologyFeedName;
    }

    public ByteArrayInputStream getTechnologyRssFeed() {
        log.info("Fetching articles from RSS feed: {}", technologyFeedName);
        return getRssFeed(technologyFeedName);
    }

    private ByteArrayInputStream getRssFeed(String feedName) {
        byte[] bytes = rssWebClient.get()
                .uri("/" + feedName)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        return new ByteArrayInputStream(Objects.requireNonNull(bytes));
    }
}
