package stage.suspectdetection.service;

import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.entities.Utilisateur;

public interface NotificationService {
//    void sendStatistics(String message);
//    void pushNotification(Object payload);
   Notification envoyerNotification(String message);
   public Notification getLastNotification();
}
