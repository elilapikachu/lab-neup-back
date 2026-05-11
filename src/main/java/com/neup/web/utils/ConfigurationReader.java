package com.neup.web.utils;

import lombok.Getter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.neup.web.utils.ConstantesEntorno.IS_RENDER;

public class ConfigurationReader {

    @Getter
    private static final Properties properties = new Properties();

    static {
        if (!IS_RENDER) {
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
    }

    public static String getProperty(String key) {
        if (IS_RENDER) {
            String envValue = System.getenv(key);
            if (envValue == null) {
                throw new RuntimeException("Variable de entorno no encontrada en Render: " + key);
            }
            return envValue;
        }
        return properties.getProperty(key);
    }
}