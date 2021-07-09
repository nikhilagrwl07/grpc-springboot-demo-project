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
import reactor.core.scheduler.Schedulers;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        userRepository.selectUserByLogin(request.getLoginId())
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
        userRepository.selectUserByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .map(user -> {
                    user.setGenre(request.getGenre().toString());
                    return user;
                })
                .flatMap(user -> userRepository.deleteByLogin(request.getLoginId())
                        .then(userRepository.saveUser(user)))
                .map(user -> UserResponse.newBuilder()
                        .setName(user.getName())
                        .setLoginId(user.getLogin())
                        .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                        .build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
