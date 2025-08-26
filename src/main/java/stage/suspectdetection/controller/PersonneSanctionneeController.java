package stage.suspectdetection.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.service.PersonneSanctionneeService;

import java.util.List;

@RestController
@RequestMapping("/api/sanctionnees")
public class PersonneSanctionneeController {

    private final PersonneSanctionneeService service;
    public PersonneSanctionneeController(PersonneSanctionneeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PersonneSanctionnee>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonneSanctionnee> getById(@PathVariable Long id) {
        PersonneSanctionnee p = service.getById(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PersonneSanctionnee> create(@RequestBody PersonneSanctionnee p) {
        PersonneSanctionnee saved = service.save(p);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonneSanctionnee> update(@PathVariable Long id, @RequestBody PersonneSanctionnee payload) {
        PersonneSanctionnee existing = service.getById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setNom(payload.getNom());
        existing.setPrenom(payload.getPrenom());
        existing.setNumeroIdentite(payload.getNumeroIdentite());
        existing.setDateNaissance(payload.getDateNaissance());
        existing.setNationalite(payload.getNationalite());
        existing.setSanctionnee(payload.isSanctionnee());
        service.save(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateStatuses(@RequestBody List<Long> idsSanctionnes) {
        service.updateSanctionnees(idsSanctionnes);
        return ResponseEntity.ok().build();
    }
}