package stage.suspectdetection.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.CasSuspect;
import stage.suspectdetection.service.CasSuspectService;

import java.util.List;

@RestController
@RequestMapping("/api/cas-suspects")
public class CasSuspectController {

    private final CasSuspectService service;
    public CasSuspectController(CasSuspectService service) { this.service = service; }

    @GetMapping("/search")
    public ResponseEntity<List<CasSuspect>> searchCasSuspects(
            @RequestParam(required = false) String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.ok(service.getAllCases());
        }

        List<CasSuspect> casSuspects = service.searchCasSuspects(searchTerm);
        return ResponseEntity.ok(casSuspects);
    }
    @GetMapping
    public ResponseEntity<List<CasSuspect>> getAll() {
        return ResponseEntity.ok(service.getAllCases());
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(service.getTotalCasSuspectsCount());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CasSuspect>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.getCasesByClientId(clientId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CasSuspect> updateStatus(@PathVariable Long id, @RequestParam String statut) {
        CasSuspect updated = service.updateStatus(id, statut);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
