package com.grpcflix.user.exception;

import com.movieservice.grpcflix.user.ErrorMessage;
import com.movieservice.grpcflix.user.UserResponseError;
import io.grpc.StatusException;
import io.grpc.protobuf.ProtoUtils;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import io.grpc.Metadata;
import io.grpc.Status;

@GrpcAdvice
public class UserExceptionHandler {

    @GrpcExceptionHandler(UserNotFoundException.class)
    public StatusException handleResourceNotFoundException(UserNotFoundException e) {
        Status status = Status.NOT_FOUND.withDescription("User not found !!").withCause(e);

        Metadata metadata = new Metadata();
        Metadata.Key<UserResponseError> errorKey = ProtoUtils.keyForProto(UserResponseError.getDefaultInstance());
        UserResponseError userResponseError = UserResponseError.newBuilder()
                .setLoginId(e.getLogin())
                .setErrorMessage(ErrorMessage.USER_NOT_FOUND)
                .build();

        metadata.put(errorKey, userResponseError);
        return status.asException(metadata);
    }
}
