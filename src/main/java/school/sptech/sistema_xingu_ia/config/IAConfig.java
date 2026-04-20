package school.sptech.sistema_xingu_ia.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IAConfig {
    @Value("${groq.api.key}")
    private String key;

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer "+key);
            requestTemplate.header("Content-Type","application/json");
        };
    }
}
