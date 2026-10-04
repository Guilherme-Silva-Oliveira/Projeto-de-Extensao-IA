package school.sptech.sistema_xingu_ia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.sistema_xingu_ia.model.InteligenciaArtificial;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteligenciaArtificialRepository extends JpaRepository<InteligenciaArtificial, Integer> {

    List<InteligenciaArtificial> findAllByOrderByTokensUtilizadosAsc();

    boolean existsByNomeModelo(String nomeModelo);

    Optional<InteligenciaArtificial> findByNomeModelo(String nomeModelo);
}
