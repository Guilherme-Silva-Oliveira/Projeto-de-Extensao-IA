package school.sptech.sistema_xingu_ia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.sistema_xingu_ia.model.Material;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> {
    Optional<Material> findByNomeMaterial(String nomeMaterial);
}
