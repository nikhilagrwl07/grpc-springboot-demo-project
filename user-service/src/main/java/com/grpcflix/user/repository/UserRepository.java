package com.grpcflix.user.repository;

import com.grpcflix.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Repository
public class UserRepository {

    @Autowired
    private ReactiveCassandraTemplate cassandraTemplate;

    public Mono<User> selectUserByLogin(String login) {
        Query query = Query.query(where("login").is(login));
        return cassandraTemplate.selectOne(query, User.class);
    }

    public Mono<Boolean> deleteByLogin(String login) {
        Query query = Query.query(where("login").is(login));
        return cassandraTemplate.delete(query, User.class);
    }

    public Mono<User> saveUser(User user) {
        return cassandraTemplate.insert(user);
    }

    public Mono<Void> deleteAllUser() {
        return cassandraTemplate.truncate(User.class);
    }

    @Transactional
    public Mono<User> updateUser(User user){
        return cassandraTemplate.update(user);
    }
}
