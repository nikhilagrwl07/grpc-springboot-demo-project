package com.grpcflix.aggregator.mapper;

import com.grpcflix.aggregator.dto.UserDetail;
import com.movieservice.grpcflix.audit.MediaStreamingRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public MediaStreamingRequest convertMediaStreamingJsonRequestToGrpcRequest(com.grpcflix.aggregator.dto.MediaStreamingRequest mediaRequest) {
        UserDetail userDetail = mediaRequest.getUserDetail();
        com.movieservice.grpcflix.audit.UserDetail userDetailGrpcRequest = com.movieservice.grpcflix.audit.UserDetail.newBuilder()
                .setLoginId(userDetail.getLoginId())
                .setName(userDetail.getName())
                .build();

        return MediaStreamingRequest.newBuilder()
                .setUserDetail(userDetailGrpcRequest)
                .build();
    }
}
