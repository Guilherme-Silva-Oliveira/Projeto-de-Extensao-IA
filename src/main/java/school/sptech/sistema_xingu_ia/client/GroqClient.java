package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.sptech.sistema_xingu_ia.config.FeignConfig;
import school.sptech.sistema_xingu_ia.dto.GroqRequest;
import school.sptech.sistema_xingu_ia.dto.GroqResponse;

@FeignClient(
        name = "groqClient",
        url = "https://api.groq.com/openai/v1",
        configuration = FeignConfig.class
)
public interface GroqClient {
    @PostMapping("/chat/completions")
    GroqResponse chat(@RequestBody GroqRequest request);
}
