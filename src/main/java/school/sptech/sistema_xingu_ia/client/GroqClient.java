package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.sptech.sistema_xingu_ia.config.IAConfig;
import school.sptech.sistema_xingu_ia.dto.ia.GroqRequest;
import school.sptech.sistema_xingu_ia.dto.ia.GroqResponse;

@FeignClient(
        name = "groqClient",
        url = "${groq.api.url}",
        configuration = IAConfig.class
)
public interface GroqClient {
    @PostMapping("/chat/completions")
    GroqResponse chat(@RequestBody GroqRequest request);
}
