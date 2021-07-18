package com.grpcflix.aggregator.service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.exception.NoMovieFoundException;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.movie.MovieSearchRequest;
import com.movieservice.grpcflix.movie.MovieSearchResponse;
import com.movieservice.grpcflix.movie.MovieServiceGrpc;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserResponse;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.grpcflix.aggregator.dto.UserResponse.*;

@Service
public class AggregatorService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public Flux<RecommendedMovie> getUserMovieSuggestions(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieStub.getMovies(movieSearchRequest);

        return Flux.fromIterable(movieSearchResponse.getMovieList())
                .map(movieDto -> {
                    if (movieDto.getTitle().isEmpty() && movieDto.getYear() == 0) { // TODO:: Use Mono's exception
                        throw new NoMovieFoundException("Movie with given genre not found!");
                    }
                    return movieDto;
                })
                .map(movieDto -> RecommendedMovie.builder()
                        .title(movieDto.getTitle())
                        .year(movieDto.getYear())
                        .rating(movieDto.getRating())
                        .build());
    }

    public Mono<com.grpcflix.aggregator.dto.UserResponse> setUserGenre(UserGenre userGenre) {
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        return Mono.just(this.userStub.updateUserGenre(userGenreUpdateRequest))
                .map(userResponse -> builder()
                        .name(userResponse.getName())
                        .genre(userResponse.getGenre().toString())
                        .loginId(userResponse.getLoginId())
                        .build());
    }
}
