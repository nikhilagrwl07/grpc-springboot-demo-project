package com.grpcflix.client.service;

import com.grpcflix.client.dto.RecommendedMovie;
import com.grpcflix.client.dto.UserGenre;
import com.grpcflix.client.dto.UserResponse;
import com.grpcflix.client.exception.RecommendedMovieErrorResponse;
import com.grpcflix.client.exception.UpdateMovieGenreErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static com.grpcflix.client.constants.AggregatorClientConstants.GET_RECOMMENDED_MOVIE_BY_LOGIN_ID;
import static com.grpcflix.client.constants.AggregatorClientConstants.SET_MOVIE_GENRE_BY_LOGIN_ID;

@Slf4j
public class AggregatorRestClientRepository {
    private WebClient webClient;

    public AggregatorRestClientRepository(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<RecommendedMovie> retrieveRecommendedMovieByLoginId(String loginId) {
//        http://localhost:8080/api/aggregator-service/user/{loginId}
        try{
            return webClient.get().uri(GET_RECOMMENDED_MOVIE_BY_LOGIN_ID, loginId)
                    .retrieve()
                    .bodyToFlux(RecommendedMovie.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException ex) { // 500 response from aggregator svc
            log.error("WebClientResponseException in retrieveRecommendedMovieByLoginId. Status code is {} and the message is {} ", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw new RecommendedMovieErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) { // Server fault
            log.error("Exception in retrieveRecommendedMovieByLoginId and the message is {} ", ex);
            throw new RecommendedMovieErrorResponse(ex);
        }
    }

    public UserResponse setUserGenre(UserGenre userGenre) {
//        http://localhost:8080/api/aggregator-service/user/{loginId}
        try{
            return webClient.put().uri(SET_MOVIE_GENRE_BY_LOGIN_ID, userGenre.getLoginId())
                    .syncBody(userGenre)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException ex) { // 500 response from aggregator svc
            log.error("WebClientResponseException in setUserGenre. Status code is {} and the message is {} ", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw new UpdateMovieGenreErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) { // Server fault
            log.error("Exception in setUserGenre and the message is {} ", ex);
            throw new UpdateMovieGenreErrorResponse(ex);
        }
    }

}
