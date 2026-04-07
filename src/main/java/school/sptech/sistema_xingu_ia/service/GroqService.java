package school.sptech.sistema_xingu_ia.service;

import org.springframework.stereotype.Service;
import school.sptech.sistema_xingu_ia.client.GroqClient;
import school.sptech.sistema_xingu_ia.client.SolicitacaoClient;
import school.sptech.sistema_xingu_ia.dto.GroqMessageStruct;
import school.sptech.sistema_xingu_ia.dto.GroqPedidoMaterial;
import school.sptech.sistema_xingu_ia.dto.GroqRequest;
import school.sptech.sistema_xingu_ia.dto.GroqResponse;
import school.sptech.sistema_xingu_ia.mapper.GroqMapper;
import school.sptech.sistema_xingu_ia.model.Material;
import school.sptech.sistema_xingu_ia.model.Professor;
import school.sptech.sistema_xingu_ia.repository.MaterialRepository;
import school.sptech.sistema_xingu_ia.repository.ProfessorRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroqService {
    private final GroqClient client;
    private final SolicitacaoClient solicitacaoClient;
    private final GroqMapper mapper;
    private final MaterialRepository materialRepository;
    private final ProfessorRepository professorRepository;
    public GroqService(GroqClient client, SolicitacaoClient solicitacaoClient, GroqMapper mapper, MaterialRepository materialRepository, ProfessorRepository professorRepository) {
        this.client = client;
        this.solicitacaoClient = solicitacaoClient;
        this.mapper = mapper;
        this.materialRepository = materialRepository;
        this.professorRepository = professorRepository;
    }
    public GroqPedidoMaterial extrairDados(String textoRecebido){
        // LISTANDO TODOS OS MATERIAIS E PROFESSORES
        List<Material> materiais = materialRepository.findAll();
        List<Professor> professores = professorRepository.findAll();

        // PASSANDO PARA LISTA EM STRING
        String listaProfessores = professores.stream()
                .map(Professor::getNome)
                .collect(Collectors.joining(","));
        String listaMateriais = materiais.stream()
                .map(Material::getNomeMaterial)
                .collect(Collectors.joining(","));

        // CONTEXTO PARA A IA
        GroqMessageStruct contextoSistema = new GroqMessageStruct();
        contextoSistema.setRole("system"); // TIVE QUE USAR IA NESSE PROMPT POIS O RETORNO É DIFÍCIL NIVELAR
        contextoSistema.setContent("""
                Você é uma IA que extrai dados estruturados.
                REGRAS OBRIGATÓRIAS:
                1. Você DEVE identificar o nome do professor EXATAMENTE como está na lista abaixo:
                   - %s
                2. Se o nome no texto NÃO for exatamente igual a um da lista, retorne:
                   "Professor não registrado"
                3. Você DEVE identificar o material EXATAMENTE como está na lista:
                   - %s
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
        """.formatted(listaProfessores,listaMateriais));

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

        // CONVERTENDO JSON PARA UMA STRING
        String json = response.getChoices()
                .getFirst()
                .getMessage()
                .getContent();
        GroqPedidoMaterial solicitacao = mapper.toGroqPedidoMaterial(json);
        solicitacaoClient.enviarSolicitacao(solicitacao);
        return solicitacao;
    }
}
