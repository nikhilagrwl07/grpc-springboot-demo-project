package com.grpcflix.movie.service;

import com.grpcflix.movie.repository.MovieRepository;
import com.movieservice.grpcflix.movie.MovieSearchRequest;
import com.movieservice.grpcflix.movie.MovieSearchResponse;
import com.movieservice.grpcflix.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(subscriber -> Flux.error(new RuntimeException("No movie with given genre found!")))
                .map(movie -> MovieSearchResponse.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating())
                        .build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
