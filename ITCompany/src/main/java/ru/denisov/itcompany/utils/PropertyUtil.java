package ru.denisov.itcompany.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyUtil {
    private static final Logger LOGGER = Logger.getLogger(PropertyUtil.class.getName());
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() throws RuntimeException {
        try (var iStream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(iStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка загрузки файла свойств: ", e);

            throw new RuntimeException(e);
        }
    }
}
