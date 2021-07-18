package com.grpcflix.aggregator.service;

import com.movieservice.grpcflix.common.Genre;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UpdateGenreResponse implements Serializable {
    private String loginId;
    private String name;
    private Genre genre;
}
