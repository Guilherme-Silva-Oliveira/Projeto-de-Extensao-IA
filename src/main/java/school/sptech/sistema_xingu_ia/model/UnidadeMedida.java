package school.sptech.sistema_xingu_ia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UnidadeMedida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeUnidade;

    public UnidadeMedida(Integer id, String nomeUnidade) {
        this.id = id;
        this.nomeUnidade = nomeUnidade;
    }
    public UnidadeMedida() {}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getNomeUnidade() {return nomeUnidade;}
    public void setNomeUnidade(String nomeUnidade) {this.nomeUnidade = nomeUnidade;}
}
