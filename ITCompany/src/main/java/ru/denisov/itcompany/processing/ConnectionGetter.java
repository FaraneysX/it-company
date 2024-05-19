package ru.denisov.itcompany.processing;

import ru.denisov.itcompany.utils.PropertyUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionGetter {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String POSTGRES_DRIVER_NAME = "org.postgresql.Driver";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final Logger LOGGER = Logger.getLogger(ConnectionGetter.class.getName());
    private static BlockingQueue<Connection> pool;

    static {
        LOGGER.log(Level.INFO, "Загрузка драйвера базы данных.");
        loadDriver();
        LOGGER.log(Level.INFO, "Драйвер базы данных загружен.");

        LOGGER.log(Level.INFO, "Инициализация пула соединений.");
        initConnectionPool();
        LOGGER.log(Level.INFO, "Пул соединений инициализирован.");
    }

    private static void initConnectionPool() {
        String poolSize = PropertyUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            Connection proxyConnection;

            try (Connection connection = open()) {
                proxyConnection = (Connection) Proxy.newProxyInstance(
                        ConnectionGetter.class.getClassLoader(),
                        new Class[]{Connection.class},
                        (proxy, method, args) -> "close".equals(method.getName()) ? pool.add((Connection) proxy) : method.invoke(connection, args)
                );

                pool.add(proxyConnection);
            } catch (SQLException | RuntimeException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());

                throw new RuntimeException(e);
            }
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(POSTGRES_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Не удалось загрузить PostgreSQL JDBC драйвер: ", e);

            throw new RuntimeException(e);
        }
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertyUtil.get(URL_KEY),
                    PropertyUtil.get(USERNAME_KEY),
                    PropertyUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Не удалось подключиться к базе данных: ", e);

            throw new RuntimeException(e);
        }
    }

    public Connection get() throws InterruptedException {
        return pool.take();
    }
}
