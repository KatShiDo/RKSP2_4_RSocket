package mirea.ru.rsocket_client.dto;

import lombok.Getter;
import lombok.Setter;
import mirea.ru.rsocket_client.entity.Computer;

import java.util.List;

@Getter
@Setter
public class ComputerListWrapper {

    private List<Computer> computers;
}
