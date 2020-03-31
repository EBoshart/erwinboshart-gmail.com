package com.gramby.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final RSocketRequester rSocketRequester;

    @GetMapping("/hello/{repeat}")
    public List<String> hello(@PathVariable int repeat) {

        return rSocketRequester.route("hello")
                .data(repeat)
                .retrieveFlux(HelloResponse.class)
                .map(response -> response.message).collectList().block();
    }

    public static class HelloResponse {
        public String message;
    }

}
