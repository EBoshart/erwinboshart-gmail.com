package com.gramby.client.controller;

import com.gramby.client.user.User;
import com.gramby.client.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
//import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HelloController {

//    private final RSocketRequester rSocketRequester;


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
//    public List<String> hello(@PathVariable int repeat) {
//
//        return rSocketRequester.route("hello")
//                .data(repeat)
//                .retrieveFlux(HelloResponse.class)
//                .map(response -> response.message).collectList().block();
//    }

    @PostMapping("/register")
    public Mono<User> register(@Valid RegistrationRequest request) {
        return Mono.just(request)
                .map(u -> new User(request.email, request.password))
                .map(u -> {u.setPassword(passwordEncoder.encode(u.getPassword())); return u;} )
                .flatMap(userService::register);
    }

    @GetMapping("/all")
    public Mono<List<User>> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello");
    }


    @RequiredArgsConstructor
    @Getter
    public static class RegistrationRequest {
        @NotNull
        public final String email;
        @NotNull
        public final String password;
    }

}
