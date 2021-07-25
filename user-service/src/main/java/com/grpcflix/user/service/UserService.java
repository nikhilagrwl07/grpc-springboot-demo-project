package com.grpcflix.user.service;

import com.grpcflix.user.entity.User;
import com.grpcflix.user.repository.UserRepository;
import com.movieservice.grpcflix.user.UserGenreUpdateRequest;
import com.movieservice.grpcflix.user.UserSearchRequest;
import com.movieservice.grpcflix.user.UserSearchResponse;
import com.movieservice.grpcflix.user.UserServiceGrpc;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.scheduler.Schedulers;

@Slf4j
@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelToViewMapper modelToViewMapper;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        Context context = Context.current();
        sleepForRequestedTime(100, context);
        User user = userRepository.selectUserByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .block();
        if (user != null) {
            UserSearchResponse userSearchResponse = modelToViewMapper.convertUserToUserResponse(user);
            responseObserver.onNext(userSearchResponse);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Requested user not found !")
                    .augmentDescription("Requested user's login id - " + request.getLoginId())
                    .asRuntimeException());
        }
    }

    private void sleepForRequestedTime(int sleepTime, Context context) {
        try {
            for (int i = 0; i < 3; i++) {
                if (!context.isCancelled()) {   // to check if client has cancelled the request
                    log.info("Server sleeping for 100 ms");
                    Thread.sleep(sleepTime);
                }
                else{
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        User user = userRepository.selectUserByLogin(request.getLoginId())
                .subscribeOn(Schedulers.boundedElastic())
                .block();

        if (user != null) {
            UserSearchResponse userSearchResponse = userRepository.updateUser(user)
                    .map(user1 -> modelToViewMapper.convertUserToUserResponse(user1))
                    .block();
            responseObserver.onNext(userSearchResponse);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Requested user not found !")
                    .augmentDescription("Requested user's login id - " + request.getLoginId())
                    .asRuntimeException());
        }
    }
}
