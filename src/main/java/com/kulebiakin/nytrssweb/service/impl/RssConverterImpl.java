package com.kulebiakin.nytrssweb.service.impl;

import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.RssConverter;
import com.rometools.rome.feed.module.DCModule;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the RssConverter using Rometools library for RSS feeds.
 * <a href="https://github.com/rometools/rome">...</a>
 */
@Component
public class RssConverterImpl implements RssConverter {

    @Override
    public List<Article> convertToArticles(InputStreamReader reader) throws Exception {
        SyndFeed feed = new SyndFeedInput().build(reader);
        return feed.getEntries().stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    private Article toArticle(SyndEntry entry) {
        String imageUrl = entry.getForeignMarkup().stream()
                .filter(element -> "content".equals(element.getName()))
                .map(element -> element.getAttributeValue("url"))
                .findAny()
                .orElse(null);

        String author = ((DCModule) entry.getModule(DCModule.URI)).getCreator();
        if (author == null || author.isBlank()) {
            author = entry.getAuthor();
        }

        List<String> categories = entry.getCategories().stream()
                .map(SyndCategory::getName)
                .toList();

        return Article.builder()
                .title(entry.getTitle())
                .link(entry.getLink())
                .description(entry.getDescription() != null ? entry.getDescription().getValue() : "")
                .imageUrl(imageUrl)
                .author(author)
                .categories(categories)
                .published(entry.getPublishedDate() != null
                        ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault())
                        : null)
                .build();
    }
}
