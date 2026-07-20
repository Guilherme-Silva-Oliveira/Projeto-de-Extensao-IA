package school.sptech.sistema_xingu_ia.dto.ia;

public class GroqPedidoMaterial {
    private String nome_professor;
    private String nome_material;
    private Integer quantidade;
    private String motivo;
    private String data_solicitacao;
    private String alerta;
    private Boolean deveDevolver;

    public GroqPedidoMaterial(String nome_professor, String nome_material, Integer quantidade, String motivo, String data_solicitacao, String alerta, Boolean deveDevolver) {
        this.nome_professor = nome_professor;
        this.nome_material = nome_material;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.data_solicitacao = data_solicitacao;
        this.alerta = alerta;
        this.deveDevolver = deveDevolver;
    }

    public GroqPedidoMaterial() {}

    public String getNome_professor() {return nome_professor;}
    public void setNome_professor(String nome_professor) {this.nome_professor = nome_professor;}
    public String getNome_material() {return nome_material;}
    public void setNome_material(String nome_material) {this.nome_material = nome_material;}
    public Integer getQuantidade() {return quantidade;}
    public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}
    public String getData_solicitacao() {return data_solicitacao;}
    public void setData_solicitacao(String data_solicitacao) {this.data_solicitacao = data_solicitacao;}
    public String getAlerta() {return alerta;}
    public void setAlerta(String alerta) {this.alerta = alerta;}
    public String getMotivo() {return motivo;}
    public void setMotivo(String motivo) {this.motivo = motivo;}
    public Boolean getDeveDevolver() {return deveDevolver;}
    public void setDeveDevolver(Boolean deveDevolver) {this.deveDevolver = deveDevolver;}
}
