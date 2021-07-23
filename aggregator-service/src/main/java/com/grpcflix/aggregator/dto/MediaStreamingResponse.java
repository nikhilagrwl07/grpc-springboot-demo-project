package com.grpcflix.aggregator.dto;

import com.movieservice.grpcflix.common.StreamingResponseStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class MediaStreamingResponse implements Serializable {
    public StreamingResponseStatus streamingResponseStatus;

}
