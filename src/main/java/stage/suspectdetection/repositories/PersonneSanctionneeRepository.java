package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.PersonneSanctionnee;
import org.springframework.stereotype.Repository;
@Repository
public interface PersonneSanctionneeRepository extends JpaRepository<PersonneSanctionnee, String> {}