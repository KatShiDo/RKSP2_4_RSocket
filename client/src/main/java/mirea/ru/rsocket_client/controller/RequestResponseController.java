package mirea.ru.rsocket_client.controller;

import lombok.RequiredArgsConstructor;
import mirea.ru.rsocket_client.entity.Computer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/computer")
@RequiredArgsConstructor
public class RequestResponseController {

    private final RSocketRequester rSocketRequester;

    @GetMapping("/{id}")
    public Mono<Computer> getComputer(@PathVariable Long id) {
        return rSocketRequester
                .route("getComputer")
                .data(id)
                .retrieveMono(Computer.class);
    }

    @PostMapping
    public Mono<Computer> addComputer(@RequestBody Computer computer) {
        return rSocketRequester
                .route("addComputer")
                .data(computer)
                .retrieveMono(Computer.class);
    }
}
