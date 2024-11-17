package mirea.ru.rsocket.repository;

import mirea.ru.rsocket.entity.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerRepository extends JpaRepository<Computer,Long> {
}
