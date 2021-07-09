package com.grpcflix.user;

import com.grpcflix.user.entity.User;
import com.grpcflix.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User nikhil = new User("nik", "ACTION", "Nikhil");
        User ritu = new User("ritu07", "DRAMA", "Ritu");

        userRepository.deleteAllUser().block();
        userRepository.saveUser(nikhil).block();
        userRepository.saveUser(ritu).block();
    }
}
