package school.sptech.sistema_xingu_ia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.sistema_xingu_ia.service.GroqService;
import school.sptech.sistema_xingu_ia.dto.ia.GroqPedidoMaterial;

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
}
