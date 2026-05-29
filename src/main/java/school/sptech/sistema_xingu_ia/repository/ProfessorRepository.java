package school.sptech.sistema_xingu_ia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.sistema_xingu_ia.model.Professor;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Integer> {

    Optional<Professor> findByNome(String nome);

}
