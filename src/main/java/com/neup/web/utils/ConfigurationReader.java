package com.neup.web.utils;

import lombok.Getter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationReader {

    @Getter
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigurationReader.class
                .getClassLoader()
                .getResourceAsStream("env.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("No se encontró env.properties en src/main/resources/");
            }
            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo de configuración", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
