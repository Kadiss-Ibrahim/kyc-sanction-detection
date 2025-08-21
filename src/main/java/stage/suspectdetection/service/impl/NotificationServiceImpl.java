package stage.suspectdetection.service.impl;

import org.springframework.stereotype.Service;
import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.entities.Utilisateur;
import stage.suspectdetection.repositories.NotificationRepository;
import stage.suspectdetection.service.NotificationService;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private Notification lastNotification;

    public NotificationServiceImpl(NotificationRepository repo) {
        this.notificationRepository = repo;
    }

    @Override
    public Notification envoyerNotification(String message) {
        Notification notif = new Notification();
        notif.setMessage(message);
        notif.setType("INFO");
        notif.setDateEnvoi(LocalDateTime.now());
        notif.setLu(false);
        this.lastNotification =notificationRepository.save(notif);
        return lastNotification;
    }
    public Notification getLastNotification() {
        return this.lastNotification;
    }

}
