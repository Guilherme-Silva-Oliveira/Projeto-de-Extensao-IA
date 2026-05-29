package school.sptech.sistema_xingu_ia.dto.ia;

import java.time.LocalDateTime;

public record SolicitacaoRequest(
        Integer idProfessor,
        String descricao,
        LocalDateTime dataSolicitacao
) {
}