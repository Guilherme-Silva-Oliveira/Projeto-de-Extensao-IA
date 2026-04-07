package school.sptech.sistema_xingu_ia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.sistema_xingu_ia.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> {}
