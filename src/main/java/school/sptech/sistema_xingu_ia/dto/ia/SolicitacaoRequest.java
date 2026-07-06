package school.sptech.sistema_xingu_ia.dto.ia;

import java.time.LocalDateTime;

public record SolicitacaoRequest(
        Integer idProfessor,
        Integer idMotivo,
        String descricao,
        LocalDateTime dataSolicitacao
) {
}
