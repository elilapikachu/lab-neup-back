package com.neup.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Expone la carpeta "uploads/" del sistema de archivos como recurso estático.
     * Las imágenes quedan accesibles en:
     *   GET /uploads/imagenes/{nombre-archivo}
     *
     * El front construye la URL completa así:
     *   const url = `${BASE_URL}/${documento.ruta}`;
     *   // dev  → http://localhost:8080/uploads/imagenes/archivo.jpg
     *   // prod → https://tu-dominio.com/uploads/imagenes/archivo.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
