package stage.suspectdetection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.service.MatchingService;
import stage.suspectdetection.service.NotificationService;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final NotificationService notificationService;

    public MatchingController(MatchingService matchingService, NotificationService notificationService) {
        this.matchingService = matchingService;
        this.notificationService = notificationService;
    }

    /**
     * Lancer le matching pour tous les clients contre toutes les personnes sanctionnées.
     * Retourne un message avec le nombre total de cas suspects détectés.
     */
    @PostMapping("/execute")
    public ResponseEntity<String> executeMatching() {
        try {
            matchingService.evaluateAllMatches(); // Cette méthode gère tous les clients et sanctions
            Notification lastNotif = notificationService.getLastNotification();
            return ResponseEntity.ok("Matching terminé : " + lastNotif.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'exécution du matching : " + e.getMessage());
        }
    }

    /**
     * Obtenir la dernière notification générée après le matching.
     */
    @GetMapping("/notification/last")
    public ResponseEntity<Notification> getLastNotification() {
        Notification notif = notificationService.getLastNotification();
        if (notif != null) {
            return ResponseEntity.ok(notif);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Tester le matching pour une personne sanctionnée spécifique
     */
    @PostMapping("/test/{sanctionId}")
    public ResponseEntity<String> testMatching(@PathVariable Long sanctionId) {
        try {
            PersonneSanctionnee p = matchingService.getSanctionneeService().getById(sanctionId);
            if (p == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Personne sanctionnée introuvable pour id = " + sanctionId);
            }
            int nbCas = matchingService.evaluateMatches(p);
            return ResponseEntity.ok("Matching pour cette personne sanctionnée : " + nbCas + " cas suspects détectés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du matching : " + e.getMessage());
        }
    }
}
