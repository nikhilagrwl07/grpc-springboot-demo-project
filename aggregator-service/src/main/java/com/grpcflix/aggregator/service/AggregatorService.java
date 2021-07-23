package com.grpcflix.aggregator.service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.dto.UserResponse;
import com.grpcflix.aggregator.mapper.RequestMapper;
import com.movieservice.grpcflix.audit.AuditServiceGrpc;
import com.movieservice.grpcflix.audit.MediaStreamingRequest;
import com.movieservice.grpcflix.audit.MediaStreamingResponse;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.common.StreamingResponseStatus;
import com.movieservice.grpcflix.movie.MovieSearchRequest;
import com.movieservice.grpcflix.movie.MovieSearchResponse;
import com.movieservice.grpcflix.movie.MovieServiceGrpc;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserSearchResponse;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class AggregatorService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    @GrpcClient("audit-service")
    private AuditServiceGrpc.AuditServiceStub auditSyncClient;

    @Autowired
    private RequestMapper requestMapper;

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

    public Mono<com.grpcflix.aggregator.dto.MediaStreamingResponse> sendAuditDetails(List<com.grpcflix.aggregator.dto.MediaStreamingRequest> mediaStreamingRequests) {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<MediaStreamingRequest> requestObserver = auditSyncClient.sendMediaStreamingDetail(new StreamObserver<MediaStreamingResponse>() {
            @Override
            public void onNext(MediaStreamingResponse value) {
                // client got a response from server
                log.info("Received a response from the server - {} ", value);
                // onNext will called only once since server will only one response
            }

            @Override
            public void onError(Throwable t) {
                // client get an error from the server
            }

            @Override
            public void onCompleted() {
                // server is done sending data to client
                log.info("Server has completed sending response to client");
                latch.countDown(); // 1 --> 0
                // oncompleted will be called right after onNext()
            }
        });
        return Flux.fromIterable(mediaStreamingRequests)
                .map(mediaStreamingRequest -> {
                    // sending data from client to server
                    log.info("Sending msg from client to server !!");
                    requestObserver.onNext(requestMapper.convertMediaStreamingJsonRequestToGrpcRequest(mediaStreamingRequest));
                    return mediaStreamingRequest;
                })
                .doOnComplete(() -> {
                    requestObserver.onCompleted();
                    try {
                        latch.await(3L, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .collectList()
                .flatMap(mediaStreamingRequestList ->
                        Mono.just(com.grpcflix.aggregator.dto.MediaStreamingResponse.builder()
                                .streamingResponseStatus(StreamingResponseStatus.ACK)
                                .build()));

    }
}
