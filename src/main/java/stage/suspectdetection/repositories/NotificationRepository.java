package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.Notification;
import org.springframework.stereotype.Repository;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {}
