package stage.suspectdetection.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        Client c = clientService.getById(id);
        return (c != null) ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client) {
        Client saved = clientService.save(client);
        return ResponseEntity.status(201).body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> saveAll(@RequestBody List<Client> clients) {
        clientService.saveAll(clients);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client payload) {
        Client existing = clientService.getById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setNom(payload.getNom());
        existing.setPrenom(payload.getPrenom());
        existing.setNumeroIdentite(payload.getNumeroIdentite());
        existing.setAdresse(payload.getAdresse());
        existing.setDateNaissance(payload.getDateNaissance());
        clientService.save(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        clientService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
