package com.grpcflix.aggregator.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RecommendedMovie implements Serializable {
    private String title;
    private int year;
    private double rating;
}
