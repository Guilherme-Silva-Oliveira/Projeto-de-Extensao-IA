package school.sptech.sistema_xingu_ia.service;

import org.springframework.stereotype.Service;
import school.sptech.sistema_xingu_ia.client.GroqClient;
import school.sptech.sistema_xingu_ia.dto.GroqMessageStruct;
import school.sptech.sistema_xingu_ia.dto.GroqPedidoMaterial;
import school.sptech.sistema_xingu_ia.dto.GroqRequest;
import school.sptech.sistema_xingu_ia.dto.GroqResponse;
import school.sptech.sistema_xingu_ia.mapper.GroqMapper;

import java.util.List;

@Service
public class GroqService {
    private final GroqClient client;
    private final GroqMapper mapper;
    public GroqService(GroqClient client, GroqMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public GroqPedidoMaterial extrairDados(String textoRecebido){
        // CONTEXTO PARA A IA
        GroqMessageStruct contextoSistema = new GroqMessageStruct();
        contextoSistema.setRole("system"); // TIVE QUE USAR IA NESSE PROMPT POIS O RETORNO É DIFÍCIL NIVELAR
        contextoSistema.setContent("""
                Você é uma IA que extrai dados estruturados.
                REGRAS OBRIGATÓRIAS:
                1. Você DEVE identificar o nome do professor EXATAMENTE como está na lista abaixo:
                   - Guilherme Silva
                   - Gabriel Furtado
                   - Pedro Giraldi
                2. Se o nome no texto NÃO for exatamente igual a um da lista, retorne:
                   "Professor não registrado"
                3. Você DEVE identificar o material EXATAMENTE como está na lista:
                   - Papel Colorido
                   - Tinta Preta
                   - Pincel Pequeno
                4. Se não encontrar, retorne:
                   "Material não registrado"
                5. Você NÃO pode inventar nomes.
                6. Você NÃO pode retornar null.
                7. Retorne APENAS um JSON válido, sem explicações.
                Formato obrigatório:
                {
                  "nome_professor": "",
                  "nome_material": "",
                  "quantidade": 0,
                  "data_solicitacao": ""
                }
        """);

        // REQUISIÇÃO DO USUÁRIO
        GroqMessageStruct contextoUsuario = new GroqMessageStruct();
        contextoUsuario.setRole("user");
        contextoUsuario.setContent("""
                Extraia os dados da seguinte solicitação:
                """ + textoRecebido);

        // UNIR MENSAGENS EM UMA LISTA
        List<GroqMessageStruct> messages = List.of(contextoSistema,contextoUsuario);

        // REQUEST PARA A IA
        GroqRequest request = new GroqRequest();
        request.setModel("llama-3.3-70b-versatile");
        request.setMessages(messages);
        request.setTemperature(0.0);
        request.setMax_completion_tokens(500);
        request.setStream(false);

        // CHAMANDO A IA
        GroqResponse response = client.chat(request);

        // CONVERTENDO JSON PARA UMA ENTIDADE
        String json = response.getChoices()
                .get(0)
                .getMessage()
                .getContent();

        return mapper.toGroqPedidoMaterial(json);
    }
}
