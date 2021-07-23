package com.grpcflix.aggregator.dto;

import com.movieservice.grpcflix.common.Channel;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MediaStreamingRequest {
    private UserDetail userDetail;
    private MovieDetail movieDetail;
    private Channel channel;
    private String timestamp;
    private Double rating;
}