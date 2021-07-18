package com.grpcflix.aggregator.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
public class UserResponse extends UserGenre{
    public String name;
}
