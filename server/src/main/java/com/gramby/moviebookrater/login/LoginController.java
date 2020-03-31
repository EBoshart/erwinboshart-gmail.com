package com.gramby.moviebookrater.login;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    @MessageMapping("hello")
    public Flux<LoginResponse> hello(@AuthenticationPrincipal Mono<UserDetails> user, int body) {
        return user.map(userDetails -> new LoginResponse("Hello " + userDetails.getUsername())).repeat(body);
//        return  Mono.just(new LoginResponse("hello " + user.map(UserDetails::getUsername)));
    }
}
