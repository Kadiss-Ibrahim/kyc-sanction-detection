package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stage.suspectdetection.entities.PersonneSanctionnee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonneSanctionneeRepository extends JpaRepository<PersonneSanctionnee, Long> {
    List<PersonneSanctionnee> findByListeSurveillanceId(Long listeId);

    @Query("SELECT p FROM PersonneSanctionnee p WHERE " +
            "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nationalite) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.adresse) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PersonneSanctionnee> searchPersonnesSanctionnees(@Param("searchTerm") String searchTerm);

}