package school.sptech.sistema_xingu_ia.service;

import org.springframework.stereotype.Service;
import school.sptech.sistema_xingu_ia.client.GroqClient;
import school.sptech.sistema_xingu_ia.dto.ia.*;
import school.sptech.sistema_xingu_ia.mapper.GroqMapper;
import school.sptech.sistema_xingu_ia.model.*;
import school.sptech.sistema_xingu_ia.repository.MaterialRepository;
import school.sptech.sistema_xingu_ia.repository.InteligenciaArtificialRepository;
import school.sptech.sistema_xingu_ia.repository.ProfessorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroqService {
    private final GroqClient client;
    private final GroqMapper mapper;
    private final MaterialRepository materialRepository;
    private final ProfessorRepository professorRepository;
    private final InteligenciaArtificialRepository modeloIARepository;

    private record ResultadoExecucaoModelo(GroqPedidoMaterial dadosIa, InteligenciaArtificial modelo, Long tokensUtilizados) {}

    public GroqService(GroqClient client, GroqMapper mapper, MaterialRepository materialRepository,
                       ProfessorRepository professorRepository, InteligenciaArtificialRepository modeloIARepository) {
        this.client = client;
        this.mapper = mapper;
        this.materialRepository = materialRepository;
        this.professorRepository = professorRepository;
        this.modeloIARepository = modeloIARepository;
    }

    public GroqPedidoMaterial extrairDados(String textoRecebido) {
        // LISTANDO TODOS OS MATERIAIS E PROFESSORES
        List<Material> materiais = materialRepository.findAll();
        List<Professor> professores = professorRepository.findAll();

        // PASSANDO PARA LISTA EM STRING
        String listaProfessores = professores.stream()
                .map(Professor::getNome)
                .collect(Collectors.joining(","));
        String listaNomesMateriais = materiais.stream()
                .map(Material::getNomeMaterial)
                .collect(Collectors.joining(","));
        String listaMateriaisComEstoque = materiais.stream()
                .map(m -> m.getNomeMaterial() + ", Quantidade Atual:" + m.getQuantidade())
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
        3. Você DEVE identificar o material EXATAMENTE como está na lista de nomes abaixo:
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
        13. O campo "deve_devolver" NUNCA pode ser vazio. Ele DEVE ser obrigatoriamente preenchido como "true" ou "false":
        - "true": se o material for durável ou de uso reutilizável que deve ser devolvido ao almoxarifado (ex: pincéis, tesouras, projetores, mouses, cabos, adaptadores, ferramentas).
        - "false": se o material for consumível ou descartável que não retorna (ex: folhas de papel sulfite, fita adesiva, detergente, copos descartáveis).
        Se houver mais de um material no pedido, separe por vírgula na mesma ordem dos materiais (ex: "true,false").
        Formato obrigatório da resposta:
        {
          "nome_professor": "",
          "nome_material": "",
          "quantidade": 0,
          "data_solicitacao": "",
          "motivo": "",
          "alerta": "",
          "deve_devolver": "true"
        }
        OBS: Salve a data_solicitacao no formato de exemplo 2026-07-20T10:00:00
        OBS2: Se houver mais de um material no pedido, adicione ambos separados por uma vírgula, ou seja, Ex: Papel,Caneta e na coluna quantidade a mesma coisa
        na mesma ordem que na coluna de nome_material adicione as respectivas quantidades.
        """.formatted(listaProfessores, listaNomesMateriais, listaMateriaisComEstoque));

        // REQUISIÇÃO DO USUÁRIO
        GroqMessageStruct contextoUsuario = new GroqMessageStruct();
        contextoUsuario.setRole("user");
        contextoUsuario.setContent("""
                Extraia os dados da seguinte solicitação:
                """ + textoRecebido);

        List<GroqMessageStruct> messages = List.of(contextoSistema, contextoUsuario);

        List<InteligenciaArtificial> modeloIAS = listarModelos();
        boolean houveSincronizacao = false;

        // Se não houver nenhum modelo cadastrado no banco, sincroniza primeiro
        if (modeloIAS.isEmpty()) {
            System.out.println("Nenhum modelo cadastrado no banco. Sincronizando com a API do Groq...");
            sincronizarModelosGroq();
            modeloIAS = listarModelos();
            houveSincronizacao = true;
        }

        // Tenta executar com os modelos atualmente cadastrados
        ResultadoExecucaoModelo resultado = tentarExecutarModelos(modeloIAS, messages);

        // Se todos os modelos cadastrados falharem e ainda não tiver sincronizado nesta requisição
        if (resultado == null && !houveSincronizacao) {
            System.out.println("Todos os modelos cadastrados falharam. Iniciando sincronização automática com a API do Groq...");
            sincronizarModelosGroq();
            modeloIAS = listarModelos();
            houveSincronizacao = true;

            // Tenta novamente com os novos modelos sincronizados
            resultado = tentarExecutarModelos(modeloIAS, messages);
        }

        // Se mesmo após a sincronização nenhum modelo responder com sucesso
        if (resultado == null) {
            throw new RuntimeException("Todos os modelos de IA (inclusive após sincronização) falharam ou estão indisponíveis no momento.");
        }

        GroqPedidoMaterial dadosIa = resultado.dadosIa();
        InteligenciaArtificial modeloEscolhido = resultado.modelo();
        Long tokensDestaRequisicao = resultado.tokensUtilizados();

        // Associa o ID do modelo utilizado na resposta para o front-end
        dadosIa.setModeloId(modeloEscolhido.getId());

        // Se houve sincronização, anexa o aviso para o front-end
        if (houveSincronizacao) {
            dadosIa.setAviso("Aviso: Os modelos cadastrados anteriormente estavam indisponíveis ou desatualizados. A lista foi sincronizada com a API do Groq automaticamente.");
        }

        // Atualiza métricas do modelo vencedor
        Long tokensAcumuladosAtuais = modeloEscolhido.getTokensUtilizados() != null ? modeloEscolhido.getTokensUtilizados() : 0L;
        modeloEscolhido.setTokensUtilizados(tokensAcumuladosAtuais + tokensDestaRequisicao);
        modeloEscolhido.setUltimaUtilizacao(LocalDateTime.now());
        modeloIARepository.save(modeloEscolhido);

        System.out.println("Métricas atualizadas para o modelo " + modeloEscolhido.getNomeModelo() +
                " (+ " + tokensDestaRequisicao + " tokens).");
        System.out.println("Sucesso completo com o modelo: " + modeloEscolhido.getNomeModelo());

        return dadosIa;
    }

    private ResultadoExecucaoModelo tentarExecutarModelos(List<InteligenciaArtificial> modelos, List<GroqMessageStruct> messages) {
        for (InteligenciaArtificial modelo : modelos) {
            try {
                System.out.println("Tentando requisição com o modelo: " + modelo.getNomeModelo());
                GroqRequest request = new GroqRequest();
                request.setModel(modelo.getNomeModelo());
                request.setMessages(messages);
                request.setTemperature(0.0);
                request.setMax_completion_tokens(800);
                request.setStream(false);

                GroqResponse response = client.chat(request);

                if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                    throw new RuntimeException("Resposta da Groq vazia para o modelo: " + modelo.getNomeModelo());
                }

                String json = response.getChoices().getFirst().getMessage().getContent();
                GroqPedidoMaterial dadosIa = mapper.toGroqPedidoMaterial(json);

                Long tokensDestaRequisicao = 0L;
                if (response.getUsage() != null) {
                    tokensDestaRequisicao = (long) response.getUsage().getTotal_tokens();
                }

                return new ResultadoExecucaoModelo(dadosIa, modelo, tokensDestaRequisicao);

            } catch (Exception e) {
                System.err.println("Falha no modelo " + modelo.getNomeModelo() + ": " + e.getMessage());
            }
        }
        return null;
    }

    public List<InteligenciaArtificial> sincronizarModelosGroq() {
        try {
            System.out.println("Consultando modelos disponíveis na API do Groq...");
            GroqModelResponse response = client.listarModelosDisponiveis();
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                System.err.println("Nenhum modelo retornado pela API do Groq.");
                return listarModelos();
            }

            List<InteligenciaArtificial> novosModelos = response.getData().stream()
                    // Apenas modelos ativos
                    .filter(m -> Boolean.TRUE.equals(m.getActive()))
                    // Apenas modelos com saída de texto (descartando modelos de voz e áudio como Whisper e Orpheus)
                    .filter(m -> m.getOutputModalities() != null && m.getOutputModalities().contains("text"))
                    .filter(m -> m.getId() != null && !m.getId().toLowerCase().contains("whisper") && !m.getId().toLowerCase().contains("orpheus"))
                    // Apenas se ainda não estiver salvo no banco
                    .filter(m -> !modeloIARepository.existsByNomeModelo(m.getId()))
                    .map(m -> {
                        InteligenciaArtificial ia = new InteligenciaArtificial();
                        ia.setNomeModelo(m.getId());
                        ia.setTokensUtilizados(0L);
                        ia.setUltimaUtilizacao(null);
                        return ia;
                    })
                    .toList();

            if (!novosModelos.isEmpty()) {
                modeloIARepository.saveAll(novosModelos);
                System.out.println("Sincronizados " + novosModelos.size() + " novos modelos do Groq no banco de dados.");
            } else {
                System.out.println("Nenhum modelo novo para sincronizar.");
            }

            return listarModelos();

        } catch (Exception e) {
            System.err.println("Erro ao sincronizar modelos com o Groq: " + e.getMessage());
            return listarModelos();
        }
    }

    public List<InteligenciaArtificial> listarModelos() {
        List<InteligenciaArtificial> modeloIAS = modeloIARepository.findAllByOrderByTokensUtilizadosAsc();
        if (modeloIAS.isEmpty()) {
            System.out.println("Não há modelos no banco");
        }
        return modeloIAS;
    }
}