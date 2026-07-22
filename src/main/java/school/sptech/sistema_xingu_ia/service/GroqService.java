package school.sptech.sistema_xingu_ia.service;

import org.springframework.stereotype.Service;
import school.sptech.sistema_xingu_ia.client.GroqClient;
import school.sptech.sistema_xingu_ia.client.SolicitacaoClient;
import school.sptech.sistema_xingu_ia.dto.ia.*;
import school.sptech.sistema_xingu_ia.mapper.GroqMapper;
import school.sptech.sistema_xingu_ia.model.*;
import school.sptech.sistema_xingu_ia.repository.MaterialRepository;
import school.sptech.sistema_xingu_ia.repository.MotivoRepository;
import school.sptech.sistema_xingu_ia.repository.InteligenciaArtificialRepository;
import school.sptech.sistema_xingu_ia.repository.ProfessorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroqService {
    private final GroqClient client;
    private final SolicitacaoClient solicitacaoClient;
    private final GroqMapper mapper;
    private final MaterialRepository materialRepository;
    private final ProfessorRepository professorRepository;
    private final MotivoRepository motivoRepository;
    private final InteligenciaArtificialRepository modeloIARepository;

    public GroqService(GroqClient client, SolicitacaoClient solicitacaoClient, GroqMapper mapper, MaterialRepository materialRepository, ProfessorRepository professorRepository, MotivoRepository motivoRepository, InteligenciaArtificialRepository modeloIARepository) {
        this.client = client;
        this.solicitacaoClient = solicitacaoClient;
        this.mapper = mapper;
        this.materialRepository = materialRepository;
        this.professorRepository = professorRepository;
        this.motivoRepository = motivoRepository;
        this.modeloIARepository = modeloIARepository;
    }

    public GroqPedidoMaterial extrairDados(String textoRecebido){
        // LISTANDO TODOS OS MATERIAIS E PROFESSORES
        List<Material> materiais = materialRepository.findAll();
        List<Professor> professores = professorRepository.findAll();
        List<Integer> quantidadeMateriais = materiais.stream().map(Material::getQuantidade).toList();

        // PASSANDO PARA LISTA EM STRING
        String listaProfessores = professores.stream()
                .map(Professor::getNome)
                .collect(Collectors.joining(","));
        String listaMateriais = materiais.stream()
                .map(m -> m.getNomeMaterial() + ", Quantidade Atual:" + m.getQuantidade())
                .collect(Collectors.joining(","));
        String quantidades = quantidadeMateriais.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // CONTEXTO PARA A IA
        GroqMessageStruct contextoSistema = new GroqMessageStruct();
        contextoSistema.setRole("system");
        contextoSistema.setContent("""
        Você é uma IA que extrai dados estruturados.
        REGRAS OBRIGATÓRIAS:
        1. Você DEVE identificar o nome do professor EXATAMENTE como está na lista abaixo:
        %s
        2. Se o nome no texto NÃO for exatamente igual a um da lista, retorne:
        "Professor não registrado"
        3. Você DEVE identificar o material EXATAMENTE como está na lista abaixo:
        %s
        4. Se não encontrar, retorne: "Material não registrado"
        5. Você NÃO pode inventar nomes.
        6. Você NÃO pode retornar null.
        7. Retorne APENAS um JSON válido, sem explicações.
        8. A lista de materiais com suas quantidades atuais está no seguinte formato: "NomeMaterial, Quantidade Atual:X"
        Lista:
        %s
        9. Para encontrar o estoque:
        - Localize na lista o material identificado
        - Extraia o número após "Quantidade Atual:"
        10. Compare a quantidade solicitada no texto com a quantidade atual:
        - Se quantidade solicitada > quantidade atual:
          alerta = "MATERIAIS_INSUFICIENTES: X material faltando"
          OBS: Se houver mais de 1 material faltando, coloque da seguinte forma: X material, Y material faltando, caso tenha mais, vai adicionando
          deixe para adicionar "faltando" no final, ou seja, ex: 10 Caneta, 20 Papel faltando
          (onde X = quantidade solicitada - quantidade atual)
        - Se quantidade solicitada == quantidade atual:
          alerta = "ESTOQUE_VAZIO: Após a solicitação, o estoque ficará sem itens"
        - Se quantidade solicitada < quantidade atual:
          alerta = "TUDO_CERTO: Material encaminhado para solicitação"
        11. O campo "alerta" NUNCA pode ser vazio ou null.
        12. Localize também o motivo da solicitação, para algo que se encaixe dentro do contexto escolar, ou seja, Atividades Avaliativas, Provas, etc
        13. Considere o campo deve_devolver analisando no contexto real se o material associado deve ser devolvido, caso seja algum material como tintas ou colas com recipiente, considere
        que não deve devolver, apenas com itens como tesouras, pincéis, que naturalmente sempre devem ser devolvidos à não ser que estejam quebrados, e preencha estes campos como false ou true sem aspas e separado
        por vírgula à cada material presente na respectiva ordem
        Formato obrigatório da resposta:
        {
          "nome_professor": "",
          "nome_material": "",
          "quantidade": 0,
          "data_solicitacao": "",
          "motivo": "",
          "alerta": "",
          "deve_devolver":""
        }
        OBS: Salve a data_solicitacao no formato de exemplo 2026-07-20T10:00:00
        OBS2: Se houver mais de um material no pedido, adicione ambos separados por uma vírgula, ou seja, Ex: Papel,Caneta e na coluna quantidade a mesma coisa
        na mesma ordem que na coluna de nome_material adicione as respectivas quantidades.
        """.formatted(listaProfessores, listaMateriais, listaMateriais));

        // REQUISIÇÃO DO USUÁRIO
        GroqMessageStruct contextoUsuario = new GroqMessageStruct();
        contextoUsuario.setRole("user");
        contextoUsuario.setContent("""
                Extraia os dados da seguinte solicitação:
                """ + textoRecebido);

        // UNIR MENSAGENS EM UMA LISTA
        List<GroqMessageStruct> messages = List.of(contextoSistema,contextoUsuario);
        List<InteligenciaArtificial> modeloIAS = listarModelos();

        // Loop pelos modelos cadastrados no banco
        for (InteligenciaArtificial modelo : modeloIAS) {
            try {
                    System.out.println("Tentando requisição com o modelo: " + modelo.getNomeModelo());
                    GroqRequest request = new GroqRequest();
                    request.setModel(modelo.getNomeModelo());
                    request.setMessages(messages);
                    request.setTemperature(0.0);
                    request.setMax_completion_tokens(500);
                    request.setStream(false);

                    GroqResponse response = client.chat(request);


                    String json = response.getChoices().getFirst().getMessage().getContent();
                    GroqPedidoMaterial dadosIa = mapper.toGroqPedidoMaterial(json);

                    Professor professorDoBanco = professorRepository.findByNome(dadosIa.getNome_professor())
                            .orElseThrow(() -> new RuntimeException("Professor extraído pela IA não está registrado no banco."));

                    Motivo motivoDoBanco;
                    if (dadosIa.getMotivo() == null || dadosIa.getMotivo().isBlank()) {
                        motivoDoBanco = motivoRepository.findById(1)
                                .orElseThrow(() -> new RuntimeException("Motivo padrao nao encontrado no banco."));
                    } else {
                        motivoDoBanco = motivoRepository.findByDescricaoIgnoreCase(dadosIa.getMotivo())
                                .orElseGet(() -> motivoRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Motivo padrao nao encontrado no banco.")));
                    }

                    LocalDateTime momentoSolicitacao = LocalDateTime.now();

                    SolicitacaoRequest novaSolicitacaoDto = new SolicitacaoRequest(
                            professorDoBanco.getId(),
                            motivoDoBanco.getId(),
                            dadosIa.getNome_material(),
                            dadosIa.getQuantidade(),
                            dadosIa.getDeveDevolver(),
                            modelo.getId(),
                            dadosIa.getMotivo(),
                            LocalDateTime.now(),
                            LocalDateTime.parse(dadosIa.getData_solicitacao())
                    );
                    solicitacaoClient.enviarSolicitacao(novaSolicitacaoDto);

                    Long tokensDestaRequisicao = (long) response.getUsage().getTotal_tokens();

                    Long tokensAcumuladosAtuais = modelo.getTokensUtilizados() != null ? modelo.getTokensUtilizados() : 0L;

                    modelo.setTokensUtilizados(tokensAcumuladosAtuais + tokensDestaRequisicao);

                    modelo.setUltimaUtilizacao(momentoSolicitacao);

                    modeloIARepository.save(modelo);

                    System.out.println("Métricas atualizadas para o modelo " + modelo.getNomeModelo() +
                            " (+ " + tokensDestaRequisicao + " tokens).");

                    System.out.println("Sucesso completo com o modelo: " + modelo.getNomeModelo());
                    return dadosIa;

                } catch (Exception e) {
                    System.err.println("Falha no fluxo do modelo " + modelo.getNomeModelo() + ". Erro: " + e.getMessage());
                }
        }
        throw new RuntimeException("Todos os modelos de IA falharam ou estão indisponíveis no momento.");
    }

    public List<InteligenciaArtificial> listarModelos() {
        List<InteligenciaArtificial> modeloIAS = modeloIARepository.findAllByOrderByTokensUtilizadosAsc();
        if (modeloIAS.isEmpty()){
            System.out.println("Não há modelos no banco");
        }
        return modeloIAS;
    }
}
