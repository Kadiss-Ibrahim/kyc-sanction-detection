package stage.suspectdetection.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.ListeSurveillance;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.service.ListeSurveillanceService;
import stage.suspectdetection.service.PersonneSanctionneeService;

import java.util.List;

@RestController
@RequestMapping("/api/listes")
public class ListeSurveillanceController {

    private final ListeSurveillanceService service;
    private final PersonneSanctionneeService personneSanctionneeService;

    public ListeSurveillanceController(ListeSurveillanceService service, PersonneSanctionneeService personneSanctionneeService) {
        this.service = service;
        this.personneSanctionneeService = personneSanctionneeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ListeSurveillance>> searchListesSurveillance(
            @RequestParam(required = false) String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.ok(service.getAll());
        }

        List<ListeSurveillance> listes = service.searchListesSurveillance(searchTerm);
        return ResponseEntity.ok(listes);
    }

    @GetMapping("/{id}/personnes")
    public List<PersonneSanctionnee> getPersonnesByListe(@PathVariable Long id) {
        return personneSanctionneeService.getByListe(id);
    }
    @PostMapping
    public ResponseEntity<ListeSurveillance> create(@RequestParam String nom, @RequestParam String source) {
        ListeSurveillance l = service.createList(nom, source);
        return ResponseEntity.status(201).body(l);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListeSurveillance> getById(@PathVariable Long id) {
        ListeSurveillance l = service.getById(id);
        return (l != null) ? ResponseEntity.ok(l) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ListeSurveillance>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getListesSurveillanceCount() {
        return ResponseEntity.ok(service.getTotalListesSurveillanceCount());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListeSurveillance> updateListe(
            @PathVariable Long id,
            @RequestParam(required = false) String nomListe,
            @RequestParam(required = false) String source) {
        try {
            ListeSurveillance updated = service.update(id, nomListe, source);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}