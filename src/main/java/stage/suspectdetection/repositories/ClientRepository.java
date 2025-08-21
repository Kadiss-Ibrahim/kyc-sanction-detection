package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.Client;
import org.springframework.stereotype.Repository;
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
