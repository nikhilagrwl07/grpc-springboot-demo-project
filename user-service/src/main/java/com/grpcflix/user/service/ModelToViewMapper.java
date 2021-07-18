package com.grpcflix.user.service;

import com.grpcflix.user.entity.User;
import com.movieservice.grpcflix.common.Genre;
import com.movieservice.grpcflix.user.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class ModelToViewMapper {

    public UserResponse convertUserToUserResponse(User user){
        return UserResponse.newBuilder()
                .setName(user.getName())
                .setLoginId(user.getLogin())
                .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                .build();
    }
}
