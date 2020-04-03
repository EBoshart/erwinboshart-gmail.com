package com.gramby.client.user;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = new CopyOnWriteArrayList<>();
        users.add(erwin());
    }

    public Mono<User> findByEmail(String email) {
        return Flux.fromIterable(users).filter(user -> user.getEmail().equals(email)).single();
    }

    public Mono<List<User>> getAll() {
        return Mono.just(users);
    }


    User erwin() {
        User user = new User();
        user.setEmail("erwinboshart@gmail.com");
        user.setPermissions(Arrays.asList(Permission.values()));
        user.setId(1L);
        user.setAge(31);
        user.setPassword("$2a$10$sF0zU4W56l2bz53B6mHHy.JdEkRGGflXu6WB9SUJlvXYS30AKzWrK");
        return user;
    }

    public Mono<User> register(User user) {
        return Mono.just(user).doOnSuccess(users::add);
    }
}
