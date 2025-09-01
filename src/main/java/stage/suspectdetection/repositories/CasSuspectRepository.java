package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stage.suspectdetection.entities.CasSuspect;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface CasSuspectRepository extends JpaRepository<CasSuspect, Long> {
    List<CasSuspect> findByClientId(Long clientId);
    @Query("SELECT c FROM CasSuspect c WHERE " +
            "LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.adresse) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CasSuspect> searchCasSuspects(@Param("searchTerm") String searchTerm);}