package cl.duoc.bffweb.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;
    private final String[] allowedMethods;
    private final String[] allowedHeaders;
    private final String[] exposedHeaders;

    public WebMvcConfig(
            @Value("${cors.allowed-origins}") String allowedOrigins,
            @Value("${cors.allowed-methods}") String allowedMethods,
            @Value("${cors.allowed-headers}") String allowedHeaders,
            @Value("${cors.exposed-headers:X-Request-Id}") String exposedHeaders) {

        this.allowedOrigins = split(allowedOrigins);
        this.allowedMethods = split(allowedMethods);
        this.allowedHeaders = split(allowedHeaders);
        this.exposedHeaders = split(exposedHeaders);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/web/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .exposedHeaders(exposedHeaders)
                .allowCredentials(false)
                .maxAge(3600);
    }

    private static String[] split(String values) {
        return Arrays.stream(values.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toArray(String[]::new);
    }
}