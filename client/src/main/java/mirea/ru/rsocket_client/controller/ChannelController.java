package mirea.ru.rsocket_client.controller;

import lombok.RequiredArgsConstructor;
import mirea.ru.rsocket_client.dto.ComputerListWrapper;
import mirea.ru.rsocket_client.entity.Computer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/computer")
@RequiredArgsConstructor
public class ChannelController {

    private final RSocketRequester rSocketRequester;

    @PostMapping("/exp")
    public Flux<Computer> addComputersMultiple(@RequestBody ComputerListWrapper dto) {
        List<Computer> computerList = dto.getComputers();
        Flux<Computer> computers = Flux.fromIterable(computerList);
        return rSocketRequester
                .route("computerChannel")
                .data(computers)
                .retrieveFlux(Computer.class);
    }
}
