package com.neup.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static com.neup.web.utils.ConstantesNumericas.SETENTA;

@Slf4j
@Component
public class StartupInfoListener {

    private final Environment env;

    public StartupInfoListener(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        
        String baseUrl = "http://localhost:" + port + contextPath;
        
        log.info("\n");
        log.info("=".repeat(SETENTA));
        log.info("✅ NEUP APPLICATION STARTED SUCCESSFULLY");
        log.info("=".repeat(SETENTA));
        log.info("\uD83D\uDCDA Swagger UI   → {}/swagger-ui/index.html", baseUrl);
        log.info("\uD83D\uDCD6 OpenAPI Docs → {}/v3/api-docs", baseUrl);
        log.info("\uD83C\uDF10 Base URL     → {}", baseUrl);
        log.info("=".repeat(SETENTA));
        log.info("\n");
    }
}