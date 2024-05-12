package ru.denisov.itcompany.singleton.connection;

import ru.denisov.itcompany.utils.PropertyUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionManager {
    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;

    static {
        loadDriver();
        initConnectionPool();
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    private static void initConnectionPool() {
        String poolSize = PropertyUtil.get("db.pool.size");
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            try (Connection connection = open()) {
                Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                        ConnectionManager.class.getClassLoader(),
                        new Class[]{Connection.class},
                        (proxy, method, args) -> "close".equals(method.getName()) ? pool.add((Connection) proxy) : method.invoke(connection, args)
                );
                pool.add(proxyConnection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Connection open() throws RuntimeException {
        try {
            return DriverManager.getConnection(
                    PropertyUtil.get(URL_KEY),
                    PropertyUtil.get(USERNAME_KEY),
                    PropertyUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подключиться к базе данных: ", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC драйвер не найден: ", e);
        }
    }

    public Connection get() throws InterruptedException {
        return pool.take();
    }
}
