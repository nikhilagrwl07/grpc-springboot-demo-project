package com.grpcflix.user.repository;

import com.grpcflix.user.entity.User;
//import com.grpcflix.user.entity.UserPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, String> {
    Mono<User> findByLogin(String loginId);
    Mono<Boolean> deleteByLogin(String loginId);
}
