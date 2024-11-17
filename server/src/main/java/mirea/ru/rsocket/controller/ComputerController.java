package mirea.ru.rsocket.controller;

import lombok.RequiredArgsConstructor;
import mirea.ru.rsocket.entity.Computer;
import mirea.ru.rsocket.repository.ComputerRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ComputerController {

    private final ComputerRepository computerRepository;

    @MessageMapping("getComputer")
    public Mono<Computer> getComputer(Long id) {
        return Mono.justOrEmpty(computerRepository.findById(id));
    }

    @MessageMapping("addComputer")
    public Mono<Computer> addComputer(Computer computer) {
        return Mono.justOrEmpty(computerRepository.save(computer));
    }

    @MessageMapping("getComputers")
    public Flux<Computer> getComputers() {
        return Flux.fromIterable(computerRepository.findAll());
    }

    @MessageMapping("deleteComputer")
    public Mono<Computer> deleteComputer(Long id){
        Optional<Computer> computer = computerRepository.findById(id);
        computerRepository.deleteById(id);
        return Mono.justOrEmpty(computer);
    }

    @MessageMapping("computerChannel")
    public Flux<Computer> computerChannel(Flux<Computer> computers){
        return computers.flatMap(computer -> Mono.fromCallable(() ->
                        computerRepository.save(computer)))
                .collectList()
                .flatMapMany(savedCats -> Flux.fromIterable(savedCats));
    }
}
