package com.grpcflix.aggregator.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {
    public String name;
    public String loginId;
    public String genre;
}
