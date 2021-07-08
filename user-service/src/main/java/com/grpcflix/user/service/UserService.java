package com.grpcflix.user.service;

//import com.grpcflix.user.entity.UserPrimaryKey;
import com.grpcflix.user.repository.UserRepository;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserResponse;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        this.userRepository.findByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .map(user -> UserResponse.newBuilder()
                        .setName(user.getName())
                        .setLoginId(user.getLogin())
                        .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                        .build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        this.userRepository.findByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .map(user -> {
                    this.userRepository.deleteByLoginAndGenre(user.getLogin(), user.getGenre()) // TBD --> not working
                            .map(user1 -> {
                                user.setGenre(request.getGenre().toString());
                                return user;
                            })
                            .then(this.userRepository.save(user));
                    return user;
                })
                .map(user -> UserResponse.newBuilder()
                        .setName(user.getName())
                        .setLoginId(user.getLogin())
                        .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                        .build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
