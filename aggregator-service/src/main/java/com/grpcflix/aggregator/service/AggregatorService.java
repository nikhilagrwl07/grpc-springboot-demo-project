package com.grpcflix.aggregator.service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.dto.UserResponse;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.movie.MovieSearchRequest;
import com.movieservice.grpcflix.movie.MovieSearchResponse;
import com.movieservice.grpcflix.movie.MovieServiceGrpc;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserSearchResponse;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
public class AggregatorService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public Flux<RecommendedMovie> getUserMovieSuggestions(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserSearchResponse userResponse = this.userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        Iterator<MovieSearchResponse> movieSearchResponses = this.movieStub.getMovies(movieSearchRequest);
        List<RecommendedMovie> result = new ArrayList<>();
        // holding on to all events
         movieSearchResponses.forEachRemaining(movie -> {
             RecommendedMovie recommendedMovie = RecommendedMovie.builder()
                     .title(movie.getTitle())
                     .year(movie.getYear())
                     .rating(movie.getRating())
                     .build();
             result.add(recommendedMovie);
             log.info("Recommended Movie - {} ", recommendedMovie.toString());
        });
        return Flux.fromIterable(result);
    }

    public Mono<com.grpcflix.aggregator.dto.UserResponse> setUserGenre(UserGenre userGenre) {
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        return Mono.just(this.userStub.updateUserGenre(userGenreUpdateRequest))
                .map(userResponse -> UserResponse.builder()
                        .name(userResponse.getName())
                        .genre(userResponse.getGenre().toString())
                        .loginId(userResponse.getLoginId())
                        .build());
    }
}
