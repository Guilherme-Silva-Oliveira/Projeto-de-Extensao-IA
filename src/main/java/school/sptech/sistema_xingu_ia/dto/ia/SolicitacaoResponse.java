package school.sptech.sistema_xingu_ia.dto.ia;

import java.time.LocalDateTime;

public record SolicitacaoResponse(
        Integer id,
        Integer idProfessor,
        String descricao,
        LocalDateTime dataSolicitacao
) {}