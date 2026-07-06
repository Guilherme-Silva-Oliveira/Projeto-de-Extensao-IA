package school.sptech.sistema_xingu_ia.model;

import jakarta.persistence.*;

@Entity
public class Material {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeMaterial;
    @ManyToOne private Categoria categoria;
    @ManyToOne private Almoxarifado almoxarifado;
    @ManyToOne private UnidadeMedida unidadeMedida;
    private Integer quantidade;
    private String descricao;

    public Material(Integer id, String nomeMaterial, Categoria categoria, Almoxarifado almoxarifado, UnidadeMedida unidadeMedida, Integer quantidade, String descricao) {
        this.id = id;
        this.nomeMaterial = nomeMaterial;
        this.categoria = categoria;
        this.almoxarifado = almoxarifado;
        this.unidadeMedida = unidadeMedida;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public Material() {}

    public String getNomeMaterial() {
        return nomeMaterial;
    }
    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }
    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}
    public Almoxarifado getEstoque() {return almoxarifado;}
    public void setEstoque(Almoxarifado estoque) {this.almoxarifado = estoque;}
    public UnidadeMedida getUnidadeMedida() {return unidadeMedida;}
    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {this.unidadeMedida = unidadeMedida;}
    public Almoxarifado getAlmoxarifado() {return almoxarifado;}
    public void setAlmoxarifado(Almoxarifado almoxarifado) {this.almoxarifado = almoxarifado;}
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
