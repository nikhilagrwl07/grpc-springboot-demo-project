package com.grpcflix.user.service;

import com.grpcflix.user.entity.User;
import com.grpcflix.user.repository.UserRepository;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserSearchResponse;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.scheduler.Schedulers;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelToViewMapper modelToViewMapper;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        userRepository.selectUserByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .defaultIfEmpty(new User(request.getLoginId(), Genre.ACTION.toString(), ""))
                .map(user -> modelToViewMapper.convertUserToUserResponse(user))
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        userRepository.selectUserByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .defaultIfEmpty(new User(request.getLoginId(), request.getGenre().toString(), ""))
                .map(user -> {
                    user.setGenre(request.getGenre().toString());
                    return user;
                })
                .flatMap(user -> userRepository.updateUser(user))
                .map(user -> modelToViewMapper.convertUserToUserResponse(user))
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
