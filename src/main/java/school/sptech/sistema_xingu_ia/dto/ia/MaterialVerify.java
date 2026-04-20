package school.sptech.sistema_xingu_ia.dto.ia;

public class MaterialVerify {
    private String nomeMaterial;
    private Integer quantidade;

    public MaterialVerify(String nomeMaterial, Integer quantidade) {
        this.nomeMaterial = nomeMaterial;
        this.quantidade = quantidade;
    }
    public MaterialVerify() {}

    public String getNomeMaterial() {return nomeMaterial;}
    public void setNomeMaterial(String nomeMaterial) {this.nomeMaterial = nomeMaterial;}
    public Integer getQuantidade() {return quantidade;}
    public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}
}
