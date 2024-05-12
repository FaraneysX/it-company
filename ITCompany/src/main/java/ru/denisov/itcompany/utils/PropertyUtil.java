package ru.denisov.itcompany.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var iStream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(iStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
