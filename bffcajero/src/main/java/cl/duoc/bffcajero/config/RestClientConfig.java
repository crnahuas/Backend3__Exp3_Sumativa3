package cl.duoc.bffcajero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(@Value("${core.base-url}") String coreBaseUrl) {

        return RestClient.builder()
                .baseUrl(coreBaseUrl)
                .build();
    }
}
