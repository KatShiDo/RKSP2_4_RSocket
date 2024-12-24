package mirea.ru.rsocket;

import io.rsocket.frame.decoder.PayloadDecoder;
import mirea.ru.rsocket.entity.Computer;
import mirea.ru.rsocket.repository.ComputerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RSocketPracticeApplicationTests {

	@Autowired
	private ComputerRepository computerRepository;

	private RSocketRequester requester;
	private Computer testComputer;

	@BeforeEach
	void setUp() {
		requester = RSocketRequester
				.builder()
				.rsocketStrategies(builder -> builder
						.decoder(new Jackson2JsonDecoder()))
				.rsocketStrategies(builder -> builder
						.encoder(new Jackson2JsonEncoder()))
				.rsocketConnector(connector -> connector
						.payloadDecoder(PayloadDecoder.ZERO_COPY)
						.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
				.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
				.tcp("localhost", 5200);
		testComputer = Computer
				.builder()
				.ram(32)
				.gpu("RTX 4070")
				.cpu("Ryzen 5 7500f")
				.memory(2)
				.build();
	}

	@AfterEach
	void cleanup() {
		requester.dispose();
	}

	@Test
	void getComputer_givenComputerId_returnsMonoComputer() {
		Computer savedComputer = computerRepository.save(testComputer);

		Mono<Computer> result = requester
				.route("getComputer")
				.data(savedComputer.getId())
				.retrieveMono(Computer.class);

		Assertions.assertNotNull(result.block());
	}

	@Test
	void addComputer_givenComputer_returnsMonoComputer() {
		Mono<Computer> result = requester
				.route("addComputer")
				.data(testComputer)
				.retrieveMono(Computer.class);

		Computer savedComputer = result.block();

		Assertions.assertNotNull(savedComputer);
		Assertions.assertNotNull(savedComputer.getId());
		Assertions.assertTrue(savedComputer.getId() > 0);
	}

	@Test
	void getComputers_returnsFluxComputer() {
		Flux<Computer> result = requester
				.route("getComputers")
				.retrieveFlux(Computer.class);

		Assertions.assertNotNull(result.blockFirst());
	}

	@Test
	void deleteComputer_givenComputerId_deletesComputer() {
		Computer savedComputer = computerRepository.save(testComputer);

		Mono<Computer> result = requester
				.route("deleteComputer")
				.data(savedComputer.getId())
				.retrieveMono(Computer.class);

		result.block();
		Optional<Computer> deletedComputer = computerRepository.findById(savedComputer.getId());
		Assertions.assertTrue(deletedComputer.isEmpty());
	}
}
