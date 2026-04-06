package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.sptech.sistema_xingu_ia.dto.GroqPedidoMaterial;

@FeignClient(name = "codigoClient",url = "http://192.168.56.1:8081")
public interface SolicitacaoClient {
    @PostMapping("/v1/ia")
    GroqPedidoMaterial enviarSolicitacao(@RequestBody GroqPedidoMaterial solicitacao);
}
