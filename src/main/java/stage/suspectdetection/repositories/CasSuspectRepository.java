package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.CasSuspect;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface CasSuspectRepository extends JpaRepository<CasSuspect, Long> {
    List<CasSuspect> findByClientId(Long clientId);
    List<CasSuspect> findByDateDetectionBetween(LocalDate startDate, LocalDate endDate);
}