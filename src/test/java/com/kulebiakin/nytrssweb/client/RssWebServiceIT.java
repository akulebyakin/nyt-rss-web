package com.kulebiakin.nytrssweb.client;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(WebClientConfig.class)
public class RssWebServiceIT {

    @RegisterExtension
    private static final WireMockExtension wm = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private RssWebService rssWebService;

    @DynamicPropertySource
    static void overrideRssUrl(DynamicPropertyRegistry reg) {
        reg.add("nytimes.rss.base-url", () -> wm.getRuntimeInfo().getHttpBaseUrl());
        reg.add("nytimes.rss.technology", () -> "Technology.xml");
    }

    @Test
    void testIntegrationRssWebService() throws IOException {
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
                </channel>
                </rss>
                """;

        wm.stubFor(get("/Technology.xml")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/rss+xml")
                        .withBody(validRssXml)));

        try (ByteArrayInputStream is = rssWebService.getTechnologyRssFeed()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).isEqualTo(validRssXml);
        }

        wm.verify(getRequestedFor(urlEqualTo("/Technology.xml")));
        wm.verify(1, getRequestedFor(urlEqualTo("/Technology.xml")));
    }
}
