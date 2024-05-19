package ru.denisov.itcompany.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.denisov.itcompany.processing.ConnectionGetter;

@UtilityClass
public class ConnectionManager {
    @Getter
    private static final ConnectionGetter connectionGetter;

    static {
        connectionGetter = new ConnectionGetter();
    }
}
