package com.kulebiakin.nytrssweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RssWebClientConfig {

    @Bean("rssWebClient")
    public WebClient rssWebClient(@Value("${nytimes.rss.base-url:https://rss.nytimes.com/services/xml/rss/nyt}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .build();
    }
}
