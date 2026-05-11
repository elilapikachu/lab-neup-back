package com.neup.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Neup Web API")
                        .version("1.0")
                        .description("Documentación de la API")
                        .contact(new Contact()
                                .name("Crazzys")
                                .email("elianaperezramos871@gmail.com")));
    }
}