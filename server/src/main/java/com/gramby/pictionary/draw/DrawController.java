package com.gramby.pictionary.draw;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    @MessageMapping("draw.push.coordinate")
    public Mono<Coordinate> push(@Valid DrawCoordinateRequest r) {
        return Mono.just(r).flatMap(request -> drawService.addCoordinate(request.x, request.y, request.drawId));
    }

    @MessageMapping("draw.get.{drawId}")
    public Flux<Coordinate> getCoordinates(@DestinationVariable Long drawId, RSocketRequester rSocketRequester) {
        return drawService.getCoordinatesForDrawing(drawId);
    }

    @MessageMapping("hello")
    public Mono<Void> hello() {
        System.out.println("hello");
        return Mono.empty();
    }

    @RequiredArgsConstructor
    @Getter
    public static class DrawCoordinateRequest {
        @NotNull
        public Integer x;
        @NotNull
        public Integer y;
        @NotNull
        public Long drawId;
    }
}
