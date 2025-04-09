package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.config.CacheConfig;
import com.kulebiakin.nytrssweb.model.Article;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(CacheConfig.class)
public class RssServiceTest {

    @Autowired
    private RssService rssService;

    @MockitoBean
    private RssConverter rssConverter;

    @DynamicPropertySource
    static void overrideRssUrl(DynamicPropertyRegistry reg) {
        reg.add("rss.technology.url", () -> "https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml");
    }

    @Test
    void testFetchTechnologyArticles() throws Exception {
        Article firstExpectedArticle = Article.builder()
                .title("Breaking News")
                .link("https://www.nytimes.com/2025/04/07/technology/breaking-news.html")
                .description("Breaking news description")
                .imageUrl("https://static01.nyt.com/images/2025/04/07/multimedia/breaing-news/breaking-news-image.jpg")
                .author("Andrei")
                .categories(List.of("Shocking News", "Life", "Social Media"))
                .published(ZonedDateTime.now())
                .build();
        Article secondExpectedArticle = Article.builder()
                .title("This is a second article")
                .link("https://www.nytimes.com/2025/04/07/technology/second-article.html")
                .description("Breaking news description")
                .author("Ivan")
                .categories(Collections.emptyList())
                .published(ZonedDateTime.now())
                .build();

        when(rssConverter.convertToArticles(ArgumentMatchers.any())).thenReturn(List.of(firstExpectedArticle, secondExpectedArticle));

        List<Article> firstArticlesFetch = rssService.fetchTechnologyArticles();

        assertThat(firstArticlesFetch.size() == 2).as("Ferch articles should return list of articles - size").isTrue();
        assertThat(firstArticlesFetch).as("Fetch articles - content").containsAll(List.of(firstExpectedArticle, secondExpectedArticle));

        List<Article> secondArticlesFetch = rssService.fetchTechnologyArticles();
        assertThat(secondArticlesFetch).as("Second Articles fetch should return same result").isEqualTo(firstArticlesFetch);

        // Check rssConverted was called only once and seond call was from cache
        verify(rssConverter, times(1)).convertToArticles(ArgumentMatchers.any());
    }

    @Test
    void testFetchTechnologyArticlesWithError() throws Exception {
        when(rssConverter.convertToArticles(ArgumentMatchers.any())).thenThrow(new RuntimeException("Some unexpected error"));

        assertThatThrownBy(() -> rssService.fetchTechnologyArticles())
                .as("Check rss converter throws exception")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Some unexpected error");

        assertThatThrownBy(() -> rssService.fetchTechnologyArticles()).isInstanceOf(RuntimeException.class);

        // Check rssConverted was called twice - no caching
        verify(rssConverter, times(2)).convertToArticles(any());
    }
}
