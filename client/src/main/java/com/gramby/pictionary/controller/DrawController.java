package com.gramby.pictionary.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.gramby.pictionary.config.JsonMetadataStrategiesCustomizer.METADATA_MIME_TYPE;

@Controller
@RequiredArgsConstructor
public class DrawController {

    private final RSocketRequester rSocketRequester;
    private final Set<RSocketRequester> connectedClients = new CopyOnWriteArraySet<>();


    @MessageMapping("draw.push.coordinate")
    public Mono<Void> push(@Valid DrawCoordinateRequest r, RSocketRequester requester) {
        connectedClients.add(requester);
        System.err.println("recieved push");
        return rSocketRequester.route("draw.push.coordinate").data(r).retrieveMono(DrawResponse.class)
                .flatMapMany(draw ->
                        Flux.fromIterable(this.connectedClients)
                                .flatMap(client -> sendDraw(client, draw))).then();
    }

    @MessageMapping("fnf")
    public Mono<Void> fireAndForget(RSocketRequester requester) {
        System.err.println("FNF");
        requester.route("test").data("FNF").send();
        return Mono.empty();
    }
//    rsocket-cli --request --metadataFormat=application/vnd.spring.rsocket.metadata+json -m='{"route":"draw.push.coordinate"}' --dataFormat=json -i='{"x":100, "y":200, "drawId":1}'  --debug ws://localhost:8080/rsocket

    @MessageMapping("draw.get.{drawId}")
    public Flux<DrawResponse> getCoordinates(@DestinationVariable Long drawId, RSocketRequester rSocketRequester) {
        connectedClients.add(rSocketRequester);
        return rSocketRequester.route("draw.get.{drawId}", drawId).retrieveFlux(DrawResponse.class);
    }
//    rsocket-cli --stream --metadataFormat=application/vnd.spring.rsocket.metadata+json -m='{"route":"draw.get.1"}' --dataFormat=json -i='{}' --debug ws://localhost:8080/rsocket

    @MessageMapping("hello")
    public Mono<Map<String,String>> testerino() {
         return rSocketRequester.route("hello").send().then(Mono.just(Map.of("result", "success")));

    }




    private Mono<Void> sendDraw(RSocketRequester requester, DrawResponse drawResponse) {
        Map<String, String> metadata = Collections.singletonMap("route", "hoi");
        System.out.println("sending");
        return requester.metadata(metadata, METADATA_MIME_TYPE).data(drawResponse).send().doOnError((x) -> connectedClients.remove(requester));
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrawResponse {
        private Long drawId;
        private int x;
        private int y;
    }
}
