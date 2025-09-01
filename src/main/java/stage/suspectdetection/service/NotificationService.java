package stage.suspectdetection.service;

import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.entities.Utilisateur;

import java.util.List;

public interface NotificationService {
   Notification envoyerNotification(String message);
   Notification getLastNotification();
   List<Notification> getAllNotifications();

}
