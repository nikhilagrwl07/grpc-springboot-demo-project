package com.grpcflix.client.service;

import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.grpcflix.client.dto.RecommendedMovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AggregatorRestClientRepositoryTest {

    AggregatorRestClientRepository aggregatorClientRepository;
    WebClient webClient;

    @InjectServer
    WireMockServer wireMockServer;

    @ConfigureWireMock
    Options options = wireMockConfig()
            .port(8088)
            .notifier(new ConsoleNotifier(true))
            .extensions(new ResponseTemplateTransformer(true));

    @BeforeEach
    void setUp() {
        int port = wireMockServer.port();
        String baseUrl = String.format("http://localhost:%s/", port);
        webClient = WebClient.create(baseUrl);
        aggregatorClientRepository = new AggregatorRestClientRepository(webClient);
//        stubFor(any(anyUrl()).willReturn(aResponse().proxiedFrom("http://localhost:8080")));
    }

    @Test
    void retrieveRecommendedMovieByLoginId() {
        // given
        stubFor(get(urlPathMatching("/api/aggregator-service/user/" + "([a-z]*)"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("recommended-movie-by-loginId.json")
                )
        );
        String loginId = "nikhilagrawal";
        //when
        List<RecommendedMovie> recommendedMovies = aggregatorClientRepository.retrieveRecommendedMovieByLoginId(loginId);

        //then
        assertEquals(2, recommendedMovies.size());
        assertEquals("Avengers first part", recommendedMovies.get(0).getTitle());
    }
}
