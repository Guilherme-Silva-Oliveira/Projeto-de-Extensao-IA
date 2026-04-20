package school.sptech.sistema_xingu_ia.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "almoxarifado")
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_sala")
    private Integer numeroSala;

    public Almoxarifado(Integer id, Integer numeroSala) {
        this.id = id;
        this.numeroSala = numeroSala;
    }
    public Almoxarifado() {}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public Integer getNumeroSala() {return numeroSala;}
    public void setNumeroSala(Integer numeroSala) {this.numeroSala = numeroSala;}
}
