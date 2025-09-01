package stage.suspectdetection.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.service.PersonneSanctionneeService;
import stage.suspectdetection.service.ListeSurveillanceService; // AJOUT

import java.util.List;

@RestController
@RequestMapping("/api/sanctionnees")
public class PersonneSanctionneeController {

    private final PersonneSanctionneeService personneSanctionneeService;
    private final ListeSurveillanceService listeSurveillanceService; // AJOUT

    // AJOUT du second paramètre dans le constructeur
    public PersonneSanctionneeController(PersonneSanctionneeService personneSanctionneeService,
                                         ListeSurveillanceService listeSurveillanceService) {
        this.personneSanctionneeService = personneSanctionneeService;
        this.listeSurveillanceService = listeSurveillanceService;
    }

    @GetMapping("/by-liste/{listeId}")
    public ResponseEntity<List<PersonneSanctionnee>> getByListe(@PathVariable Long listeId) {
        // Vérifier que la liste existe d'abord
        if (listeSurveillanceService.getById(listeId) == null) {
            return ResponseEntity.notFound().build();
        }

        List<PersonneSanctionnee> personnes = personneSanctionneeService.getByListe(listeId);
        return ResponseEntity.ok(personnes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonneSanctionnee>> searchPersonnesSanctionnees(
            @RequestParam(required = false) String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.ok(personneSanctionneeService.getAll());
        }

        List<PersonneSanctionnee> personnes = personneSanctionneeService.searchPersonnesSanctionnees(searchTerm);
        return ResponseEntity.ok(personnes);
    }

    @GetMapping
    public ResponseEntity<List<PersonneSanctionnee>> getAll() {
        return ResponseEntity.ok(personneSanctionneeService.getAll());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getPersonnesSanctionneesCount() {
        return ResponseEntity.ok(personneSanctionneeService.getTotalPersonnesSanctionneesCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonneSanctionnee> getById(@PathVariable Long id) {
        PersonneSanctionnee p = personneSanctionneeService.getById(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PersonneSanctionnee> create(@RequestBody PersonneSanctionnee p) {
        // Validation: vérifier que la liste de surveillance existe
        if (p.getListeSurveillance() == null || p.getListeSurveillance().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Long listeId = p.getListeSurveillance().getId();
        if (listeSurveillanceService.getById(listeId) == null) {
            return ResponseEntity.notFound().build();
        }

        PersonneSanctionnee saved = personneSanctionneeService.save(p);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (personneSanctionneeService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        personneSanctionneeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonneSanctionnee> updatePersonne(
            @PathVariable Long id,
            @RequestBody PersonneSanctionnee personne) {

        // Vérifier que la personne existe
        if (personneSanctionneeService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        // Validation: vérifier que la nouvelle liste existe si elle est fournie
        if (personne.getListeSurveillance() != null && personne.getListeSurveillance().getId() != null) {
            Long listeId = personne.getListeSurveillance().getId();
            if (listeSurveillanceService.getById(listeId) == null) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(personneSanctionneeService.updatePersonne(id, personne));
    }
}