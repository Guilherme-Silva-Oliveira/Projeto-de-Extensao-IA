package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.sptech.sistema_xingu_ia.dto.ia.SolicitacaoResponse;
import school.sptech.sistema_xingu_ia.model.SolicitacaoRequest;

@FeignClient(name = "solicitacaoClient", url = "${solicitacao-client.api.url}")
public interface SolicitacaoClient {

    @PostMapping("/v1/solicitacoes")
    SolicitacaoResponse enviarSolicitacao(@RequestBody SolicitacaoRequest solicitacao);
}