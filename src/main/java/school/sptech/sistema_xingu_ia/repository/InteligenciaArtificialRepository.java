package school.sptech.sistema_xingu_ia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.sistema_xingu_ia.model.InteligenciaArtificial;

import java.util.List;

@Repository
public interface InteligenciaArtificialRepository extends JpaRepository<InteligenciaArtificial,Integer> {

    List<InteligenciaArtificial> findAllByOrderByTokensUtilizadosAsc();

}
