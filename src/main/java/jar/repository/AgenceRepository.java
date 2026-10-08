package jar.repository;

import jar.entity.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenceRepository extends JpaRepository<Agence, Long> {
}