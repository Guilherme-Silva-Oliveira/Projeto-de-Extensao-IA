package school.sptech.sistema_xingu_ia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.sistema_xingu_ia.model.InteligenciaArtificial;
import school.sptech.sistema_xingu_ia.service.GroqService;
import school.sptech.sistema_xingu_ia.service.LambdaService;
import school.sptech.sistema_xingu_ia.dto.ia.GroqPedidoMaterial;

import java.util.List;

@RestController
@RequestMapping("/ia")
@Tag(name = "Inteligência Artificial", description = "Operações de processamento de texto e modelos de IA")
public class GroqController {
    private final GroqService service;
    private final LambdaService lambdaService;

    public GroqController(GroqService service, LambdaService lambdaService) {
        this.service = service;
        this.lambdaService = lambdaService;
    }

    @Operation(summary = "Extrair dados de solicitação de material via IA")
    @PostMapping("/talk")
    public ResponseEntity<GroqPedidoMaterial> talk(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Texto da solicitação do professor",
                    required = true,
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo 1 - Pincel Marcador",
                                            description = "Solicitação com professor Roberto Alves e Pincel Marcador Azul",
                                            value = "O professor Roberto Alves solicitou 5 Pincel Marcador Azul para aula amanhã"
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo 2 - Folha Sulfite",
                                            description = "Solicitação com professora Ana Beatriz e Folha A4 Sulfite 75g",
                                            value = "A professora Ana Beatriz Santos precisa de 10 Folha A4 Sulfite 75g para a prova amanhã"
                                    )
                            }
                    )
            )
            @RequestBody String mensagem

    ) {

        GroqPedidoMaterial resposta = service.extrairDados(mensagem);

        lambdaService.enviarParaS3(resposta);
        return ResponseEntity.status(201).body(service.extrairDados(mensagem));
    }

    @Operation(summary = "Listar modelos de IA cadastrados")
    @GetMapping("/talk")
    public ResponseEntity<List<InteligenciaArtificial>> listarModelos() {
        return ResponseEntity.status(200).body(service.listarModelos());
    }

    @Operation(summary = "Forçar sincronização com a API do Groq")
    @PostMapping("/sincronizar-modelos")
    public ResponseEntity<List<InteligenciaArtificial>> sincronizarModelos() {
        return ResponseEntity.ok(service.sincronizarModelosGroq());
    }
}
