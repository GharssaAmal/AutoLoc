package jar.repository;

import jar.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
}