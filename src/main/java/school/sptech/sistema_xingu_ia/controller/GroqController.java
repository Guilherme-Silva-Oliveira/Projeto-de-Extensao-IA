package school.sptech.sistema_xingu_ia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.sistema_xingu_ia.model.InteligenciaArtificial;
import school.sptech.sistema_xingu_ia.service.GroqService;
import school.sptech.sistema_xingu_ia.dto.ia.GroqPedidoMaterial;

import java.util.List;

@RestController
@RequestMapping("/ia")
public class GroqController {
    private final GroqService service;
    public GroqController(GroqService service) {
        this.service = service;
    }

    @PostMapping("/talk")
    public ResponseEntity<GroqPedidoMaterial> talk (@RequestBody String mensagem){
        return ResponseEntity.status(201).body(service.extrairDados(mensagem));
    }

    @GetMapping("/talk")
    public ResponseEntity<List<InteligenciaArtificial>> listarModelos(){
        return ResponseEntity.status(200).body(service.listarModelos());
    }

}
