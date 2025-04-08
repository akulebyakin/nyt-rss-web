package com.kulebiakin.nytrssweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String title;
    private String link;
    private String description;
    private String imageUrl;
    private String author;
    private List<String> categories;
    private ZonedDateTime published;
}
