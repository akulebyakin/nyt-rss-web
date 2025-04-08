package com.kulebiakin.nytrssweb.service;

import com.kulebiakin.nytrssweb.model.Article;
import com.rometools.rome.feed.module.DCModule;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RssService {
    @Value("${rss.technology.url}")
    private String technologyRssUrl;

    @Cacheable("articles")
    public List<Article> fetchArticles() throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
        try (InputStreamReader reader = new InputStreamReader(new URL(technologyRssUrl).openStream())) {
            feed = input.build(reader);
        }

        return feed.getEntries().stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    private Article toArticle(SyndEntry entry) {
        Optional<Element> imageUrlOptional = entry.getForeignMarkup()
                .stream()
                .filter(element -> "content".equals(element.getName()))
                .findAny();
        String imageUrl = null;
        if (imageUrlOptional.isPresent()) {
            imageUrl = imageUrlOptional.get().getAttributeValue("url");
        }

        String author = null;
        Module dcModule = entry.getModule(DCModule.URI);
        if (dcModule instanceof DCModule dc) {
            author = dc.getCreator();
        }
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
                .published(entry.getPublishedDate() != null ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()) : null)
                .build();
    }
}
