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
        LOGGER.log(Level.INFO, "Loading the database driver.");
        loadDriver();
        LOGGER.log(Level.INFO, "The database driver is loaded.");

        LOGGER.log(Level.INFO, "Initializing the connection pool.");
        initConnectionPool();
        LOGGER.log(Level.INFO, "The connection pool has been initialized.");
    }

    private static void initConnectionPool() {
        String poolSize = PropertyUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionGetter.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> "close".equals(method.getName()) ? pool.add((Connection) proxy) : method.invoke(connection, args)
            );
            pool.add(proxyConnection);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(POSTGRES_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to load PostgreSQL JDBC driver:", e);

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
            LOGGER.log(Level.SEVERE, "Failed to connect to database: ", e);

            throw new RuntimeException(e);
        }
    }

    public Connection get() throws InterruptedException {
        return pool.take();
    }
}
