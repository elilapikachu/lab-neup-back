package com.neup.web.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.neup.web.utils.ConstantesUrls.URL_ENV_PROPERTIES;

public class ConfigurationReader {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(URL_ENV_PROPERTIES)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo de configuración: " + URL_ENV_PROPERTIES, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static Properties getProperties() {
        return properties;
    }
}
