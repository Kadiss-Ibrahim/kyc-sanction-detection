package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stage.suspectdetection.entities.ListeSurveillance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListeSurveillanceRepository extends JpaRepository<ListeSurveillance, Long> {
    @Query("SELECT l FROM ListeSurveillance l WHERE " +
            "LOWER(l.nomListe) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(l.source) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ListeSurveillance> searchListesSurveillance(@Param("searchTerm") String searchTerm);
}