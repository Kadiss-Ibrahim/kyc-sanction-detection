package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.ListeSurveillance;
import org.springframework.stereotype.Repository;
@Repository
public interface ListeSurveillanceRepository extends JpaRepository<ListeSurveillance, Long> {

}