package mirea.ru.rsocket_client.controller;

import lombok.RequiredArgsConstructor;
import mirea.ru.rsocket_client.entity.Computer;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/computer")
@RequiredArgsConstructor
public class RequestStreamController {

    private final RSocketRequester rSocketRequester;

    @GetMapping
    public Flux<Computer> getComputers() {
        return rSocketRequester
                .route("getComputers")
                .data(new Computer())
                .retrieveFlux(Computer.class);
    }
}
