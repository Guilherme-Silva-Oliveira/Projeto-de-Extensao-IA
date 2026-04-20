package school.sptech.sistema_xingu_ia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeCategoria;

    public Categoria(Integer id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }
    public Categoria() {}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getNomeCategoria() {return nomeCategoria;}
    public void setNomeCategoria(String nomeCategoria) {this.nomeCategoria = nomeCategoria;}
}
