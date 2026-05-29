package com.neup.web.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> patterns = new ArrayList<>(List.of(
                "http://localhost:4200",
                "http://localhost:4201",
                "https://*.vercel.app"
        ));
        String extraOrigins = System.getenv("ALLOWED_ORIGINS");
        if (extraOrigins != null && !extraOrigins.isBlank()) {
            patterns.addAll(Arrays.asList(extraOrigins.split(",")));
        }
        config.setAllowedOriginPatterns(patterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        // ✅ Agregar rutas de Swagger
        source.registerCorsConfiguration("/swagger-ui/**", config);
        source.registerCorsConfiguration("/v3/api-docs/**", config);
        source.registerCorsConfiguration("/swagger-ui.html", config);

        return new CorsFilter(source);
    }
}
