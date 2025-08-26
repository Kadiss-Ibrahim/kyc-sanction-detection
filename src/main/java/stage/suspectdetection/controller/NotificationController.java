package stage.suspectdetection.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.service.NotificationService;

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

    @PostMapping("/send")
    public ResponseEntity<Notification> send(@RequestParam String message) {
        Notification n = service.envoyerNotification(message);
        return ResponseEntity.status(201).body(n);
    }
}
