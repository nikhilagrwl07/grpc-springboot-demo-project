package com.grpcflix.user.service;

import com.grpcflix.user.entity.User;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.user.UserSearchResponse;
import org.springframework.stereotype.Service;

@Service
public class ModelToViewMapper {

    public UserSearchResponse convertUserToUserResponse(User user){
        return UserSearchResponse.newBuilder()
                .setName(user.getName())
                .setLoginId(user.getLogin())
                .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                .build();
    }
}
