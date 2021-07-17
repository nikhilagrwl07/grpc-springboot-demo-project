package com.grpcflix.client.dto;

import lombok.Data;

@Data
public class UserResponse extends UserGenre{
    String name;
}
