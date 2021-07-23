package com.grpcflix.audit.service;

import com.movieservice.grpcflix.audit.AuditServiceGrpc;
import com.movieservice.grpcflix.audit.MediaStreamingRequest;
import com.movieservice.grpcflix.audit.MediaStreamingResponse;
import com.movieservice.grpcflix.common.StreamingResponseStatus;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class AuditService extends AuditServiceGrpc.AuditServiceImplBase {
    @Override
    public StreamObserver<MediaStreamingRequest> sendMediaStreamingDetail(StreamObserver<MediaStreamingResponse> responseObserver) {
        StreamObserver<MediaStreamingRequest> requestObserver = new StreamObserver<MediaStreamingRequest>() {
            String collectedAuditDetails = "";

            @Override
            public void onNext(MediaStreamingRequest mediaStreamingRequest) {
                // client sends a message
                collectedAuditDetails += mediaStreamingRequest.toString() + " \n";
            }

            @Override
            public void onError(Throwable t) {
                // client sends an error
            }

            @Override
            public void onCompleted() {
                // client is done
                log.info("Audit detail send by client :: " + collectedAuditDetails);
                responseObserver.onNext(MediaStreamingResponse
                        .newBuilder().setStreamingResponseStatus(StreamingResponseStatus.ACK).build());
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}
