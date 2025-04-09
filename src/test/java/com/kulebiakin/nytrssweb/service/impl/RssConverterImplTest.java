package com.kulebiakin.nytrssweb.service.impl;

import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.RssConverter;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RssConverterImplTest {

    private final RssConverter rssConverter = new RssConverterImpl();

    private static InputStreamReader getInputStreamReader(String rssXmlWithNoImageAndNoUrl) {
        return new InputStreamReader(
                new ByteArrayInputStream(rssXmlWithNoImageAndNoUrl.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @Test
    void testConvertValidRssXml() throws Exception {
        String validRssXml = """
                <rss xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:media="http://search.yahoo.com/mrss/" version="2.0">
                <channel>
                <item>
                <title>News Title 1</title>
                <link>news-url-1</link>
                <description>Test Description 1</description>
                <dc:creator>Cecilia Kang</dc:creator>
                <pubDate>Tue, 08 Apr 2025 09:01:22 +0000</pubDate>
                <category>Category 1</category>
                <category>Category 2</category>
                <media:content url="image-url-1"/>
                </item>
                <item>
                <title>News Title 2</title>
                <link>news-url-2</link>
                <description>News Description 2</description>
                <dc:creator>Lauren Hirsch</dc:creator>
                <pubDate>Wed, 09 Apr 2025 06:37:07 +0000</pubDate>
                <category>Category 2</category>
                <category>Category 3</category>
                <media:content url="image-url-2"/>
                </item>
                </channel>
                </rss>
                """;

        try (InputStreamReader reader = getInputStreamReader(validRssXml)) {
            List<Article> articles = rssConverter.convertToArticles(reader);

            assertThat(articles).hasSize(2);

            Article first = articles.get(0);
            assertThat(first.getTitle()).isEqualTo("News Title 1");
            assertThat(first.getLink()).isEqualTo("news-url-1");
            assertThat(first.getDescription()).isEqualTo("Test Description 1");
            assertThat(first.getImageUrl()).isEqualTo("image-url-1");
            assertThat(first.getAuthor()).isEqualTo("Cecilia Kang");
            assertThat(first.getCategories()).containsExactly("Category 1", "Category 2");
            assertThat(first.getPublished()).isEqualTo(ZonedDateTime.parse(
                    "Tue, 08 Apr 2025 09:01:22 +0000", DateTimeFormatter.RFC_1123_DATE_TIME));

            Article second = articles.get(1);
            assertThat(second.getTitle()).isEqualTo("News Title 2");
            assertThat(second.getLink()).isEqualTo("news-url-2");
            assertThat(second.getDescription()).isEqualTo("News Description 2");
            assertThat(second.getImageUrl()).isEqualTo("image-url-2");
            assertThat(second.getAuthor()).isEqualTo("Lauren Hirsch");
            assertThat(second.getCategories()).containsExactly("Category 2", "Category 3");
            assertThat(second.getPublished()).isEqualTo(ZonedDateTime.parse(
                    "Wed, 09 Apr 2025 06:37:07 +0000", DateTimeFormatter.RFC_1123_DATE_TIME));
        }
    }

    @Test
    void testConvertRssXmlWithNoImageAndNoLink() throws Exception {
        String rssXmlWithNoImageAndNoUrl = """
                <rss xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:media="http://search.yahoo.com/mrss/" version="2.0">
                <channel>
                <item>
                <title>News Title Without Image and link</title>
                <description>Description Without Image and link</description>
                <dc:creator>Author Without Image and link</dc:creator>
                <pubDate>Fri, 11 Apr 2025 15:00:00 +0000</pubDate>
                <category>Category Without Image and link</category>
                </item>
                </channel>
                </rss>
                """;

        try (InputStreamReader reader = getInputStreamReader(rssXmlWithNoImageAndNoUrl)) {
            List<Article> articles = rssConverter.convertToArticles(reader);

            assertThat(articles).hasSize(1);

            Article article = articles.get(0);
            assertThat(article.getTitle()).isEqualTo("News Title Without Image and link");
            assertThat(article.getLink()).isNull();
            assertThat(article.getDescription()).isEqualTo("Description Without Image and link");
            assertThat(article.getImageUrl()).isNull();
            assertThat(article.getAuthor()).isEqualTo("Author Without Image and link");
            assertThat(article.getCategories()).containsExactly("Category Without Image and link");
            assertThat(article.getPublished()).isEqualTo(ZonedDateTime.parse(
                    "Fri, 11 Apr 2025 15:00:00 +0000", DateTimeFormatter.RFC_1123_DATE_TIME));
        }
    }

    @Test
    void testConvertValidRssXmlWithEmptyArticlesList() throws Exception {
        String rssNoArticles = """
                <rss xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:media="http://search.yahoo.com/mrss/" version="2.0">
                <channel>
                <title>Empty feed</title>
                <description>No articles here</description>
                <link>news-url-1</link>
                </channel>
                </rss>
                """;

        try (InputStreamReader reader = getInputStreamReader(rssNoArticles)) {
            List<Article> articles = rssConverter.convertToArticles(reader);

            assertThat(articles).as("RSS feed is empty").isEmpty();
        }
    }

    @Test
    void testConvertRssXmlEmptyString() throws IOException {
        String emptyXml = "";
        try (InputStreamReader reader = getInputStreamReader(emptyXml)) {
            assertThatThrownBy(() -> rssConverter.convertToArticles(reader))
                    .isInstanceOf(FeedException.class)
                    .hasMessageContaining("Invalid XML");
        }
    }
}
