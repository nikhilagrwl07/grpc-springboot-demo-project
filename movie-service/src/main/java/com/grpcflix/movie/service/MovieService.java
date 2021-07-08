package com.grpcflix.movie.service;

import com.grpcflix.movie.repository.MovieRepository;
import com.movieservice.grpcflix.movie.MovieDto;
import com.movieservice.grpcflix.movie.MovieSearchRequest;
import com.movieservice.grpcflix.movie.MovieSearchResponse;
import com.movieservice.grpcflix.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .subscribeOn(Schedulers.boundedElastic())
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating())
                        .build())
                .collectList()
                .flatMap(movieDtos -> {
                    MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovie(movieDtos).build();
                    return Mono.just(movieSearchResponse);
                })
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
