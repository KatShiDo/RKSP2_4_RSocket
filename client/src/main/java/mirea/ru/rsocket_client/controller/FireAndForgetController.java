package mirea.ru.rsocket_client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/computer")
@RequiredArgsConstructor
public class FireAndForgetController {

    private final RSocketRequester rSocketRequester;

    @DeleteMapping("/{id}")
    public Publisher<Void> deleteComputer(@PathVariable Long id) {
        return rSocketRequester
                .route("deleteComputer")
                .data(id)
                .send();
    }
}
