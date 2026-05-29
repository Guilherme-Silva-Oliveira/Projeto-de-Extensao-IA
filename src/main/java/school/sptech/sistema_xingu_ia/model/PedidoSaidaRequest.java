package school.sptech.sistema_xingu_ia.model;

import java.time.LocalDateTime;

public class PedidoSaidaRequest {

    private Integer materialId;
    private Integer solicitacaoId;
    private Integer quantidade;
    private LocalDateTime dataSolicitacao;
    private Integer escalaId;


    public PedidoSaidaRequest(Integer materialId, Integer solicitacaoId, Integer quantidade, LocalDateTime dataSolicitacao, Integer escalaId) {
        this.materialId = materialId;
        this.solicitacaoId = solicitacaoId;
        this.quantidade = quantidade;
        this.dataSolicitacao = dataSolicitacao;
        this.escalaId = escalaId;
    }

    public PedidoSaidaRequest() {
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getSolicitacaoId() {
        return solicitacaoId;
    }

    public void setSolicitacaoId(Integer solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Integer getEscalaId() {
        return escalaId;
    }

    public void setEscalaId(Integer escalaId) {
        this.escalaId = escalaId;
    }
}
