package mirea.ru.rsocket_client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Computer {

    private long id;

    private String cpu;

    private String gpu;

    private int ram;

    private int memory;
}

