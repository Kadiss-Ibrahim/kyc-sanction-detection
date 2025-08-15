package stage.suspectdetection.service;

public interface NotificationService {
    void sendStatistics(String message);
    void pushNotification(Object payload); // adapte le type payload selon ton implémentation
}
