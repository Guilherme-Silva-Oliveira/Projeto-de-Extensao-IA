package school.sptech.sistema_xingu_ia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SolicitacaoRequest(
        @NotNull @Schema(description = "Fk para Professor",example = "1") Integer idProfessor,
        @NotNull @Schema(description = "Fk para Motivo",example = "1") Integer idMotivo,
        @NotNull @Schema(description = "Fk para Material",example = "1") Integer idMaterial,
        @NotNull @Schema(description = "Quantidade de Material",example = "1") Integer quantidade,
        @Schema(description = "Fk para modelo de IA utilizado") Integer inteligenciaArtificialId,
        @NotBlank @Schema(description = "Motivo da Solicitação",example = "Atividade Avaliativa")String descricao,
        @NotNull @Schema(description = "Data da Solicitação",example = "20-04-2026") LocalDateTime dataSolicitacao,
        @NotNull @Schema(description = "Data da Solicitação",example = "20-04-2026") LocalDateTime dataParaEnvio
) {}
