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

        userRepository.deleteAll().block();
        userRepository.save(nikhil).block();

        userRepository.deleteByLogin(nikhil.getLogin())
                .map(result -> {
//                    System.out.println("Result : " + result);
                    nikhil.setGenre("CRIME");
                    return nikhil;
                })
                .then(this.userRepository.save(nikhil))
                .block();

        userRepository.save(ritu).block();
    }
}
