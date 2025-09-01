package stage.suspectdetection.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }

    @GetMapping("/last")
    public ResponseEntity<Notification> getLast() {
        Notification last = service.getLastNotification();
        return (last != null) ? ResponseEntity.ok(last) : ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAll() {
        List<Notification> notifications = service.getAllNotifications();
        return notifications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(notifications);
    }
}
