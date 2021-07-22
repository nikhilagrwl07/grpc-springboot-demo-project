package com.grpcflix.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
//@SuperBuilder
@Data
public class UserGenre {
    public String loginId;
    public String genre;
}
