package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.sptech.sistema_xingu_ia.model.PedidoSaidaRequest;

@FeignClient(name = "pedido-saida-client", url = "${pedido-saida.api.url}")
public interface PedidoSaidaClient {

    @PostMapping("/v1/saidas")
    void cadastrarPedidoSaida(@RequestBody PedidoSaidaRequest request);
}