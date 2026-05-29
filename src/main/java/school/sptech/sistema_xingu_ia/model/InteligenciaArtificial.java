package school.sptech.sistema_xingu_ia.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class InteligenciaArtificial {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeModelo;
    private Long tokensUtilizados;
    private LocalDateTime ultimaUtilizacao;

    public InteligenciaArtificial(Integer id, String nomeModelo, Long tokensUtilizados, LocalDateTime ultimaUtilizacao) {
        this.id = id;
        this.nomeModelo = nomeModelo;
        this.tokensUtilizados = tokensUtilizados;
        this.ultimaUtilizacao = ultimaUtilizacao;
    }

    public InteligenciaArtificial() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public Long getTokensUtilizados() {
        return tokensUtilizados;
    }

    public void setTokensUtilizados(Long tokensUtilizados) {
        this.tokensUtilizados = tokensUtilizados;
    }

    public LocalDateTime getUltimaUtilizacao() {
        return ultimaUtilizacao;
    }

    public void setUltimaUtilizacao(LocalDateTime ultimaUtilizacao) {
        this.ultimaUtilizacao = ultimaUtilizacao;
    }
}
