package com.kulebiakin.nytrssweb.controller;

import com.kulebiakin.nytrssweb.model.Article;
import com.kulebiakin.nytrssweb.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ArticleController.class)
public class ArticleControllerTest {

    private final static String TEST_RSS_TECHNOLOGY_URL = "/api/articles/technology";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Test
    void testReturnArticlesSuccessfully() throws Exception {
        Article article = Article.builder()
                .title("Breaking News")
                .link("https://www.nytimes.com/2025/04/07/technology/breaking-news.html")
                .description("Breaking news description")
                .imageUrl("https://static01.nyt.com/images/2025/04/07/multimedia/breaing-news/breaking-news-image.jpg")
                .author("Andrei")
                .categories(List.of("Shocking News", "Life", "Social Media"))
                .published(ZonedDateTime.now())
                .build();

        when(articleService.fetchTechnologyArticles()).thenReturn(List.of(article));

        mockMvc.perform(get(TEST_RSS_TECHNOLOGY_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Breaking News"))
                .andExpect(jsonPath("$[0].link").value("https://www.nytimes.com/2025/04/07/technology/breaking-news.html"))
                .andExpect(jsonPath("$[0].description").value("Breaking news description"))
                .andExpect(jsonPath("$[0].imageUrl").value("https://static01.nyt.com/images/2025/04/07/multimedia/breaing-news/breaking-news-image.jpg"))
                .andExpect(jsonPath("$[0].author").value("Andrei"))
                .andExpect(jsonPath("$[0].categories[0]").value("Shocking News"))
                .andExpect(jsonPath("$[0].categories[1]").value("Life"))
                .andExpect(jsonPath("$[0].categories[2]").value("Social Media"));
    }

    @Test
    void testReturnsError() throws Exception {
        when(articleService.fetchTechnologyArticles()).thenThrow(new RuntimeException("This is some unexpected error"));

        mockMvc.perform(get(TEST_RSS_TECHNOLOGY_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("This is some unexpected error"))
                .andExpect(jsonPath("$.error").value("RuntimeException"));
    }
}
