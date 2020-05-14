package com.gramby.pictionary.draw;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DrawService {

    private final List<Coordinate> coordinates = new ArrayList<>(initialCoordinates());
    private Random random = new Random();

    public Flux<Coordinate> getCoordinatesForDrawing(Long drawId) {
        return Flux.fromIterable(coordinates).filter(c -> c.getDrawId().equals(drawId));
    }

    public Mono<Coordinate> addCoordinate(int x, int y, Long drawId) {
        var coordinate = new Coordinate(drawId, x, y );
        coordinates.add(coordinate);
        return Mono.just(coordinate);
    }

    public List<Coordinate> initialCoordinates() {
        return List.of(
        new Coordinate(1L,5,5),
        new Coordinate(1L,5,6),
        new Coordinate(1L,5,7),
        new Coordinate(1L,6,7),
        new Coordinate(1L,6,8));
    }
}
