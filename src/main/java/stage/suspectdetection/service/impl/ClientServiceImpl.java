package stage.suspectdetection.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.repositories.ClientRepository;
import stage.suspectdetection.service.ClientService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repo;

    public ClientServiceImpl(ClientRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Client> getAllClients() {
        return repo.findAll();
    }

    @Override
    public Client getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Client save(Client c) {
        return repo.save(c);
    }
    @Override
    @Transactional
    public void saveAll(List<Client> clients) {
        for (Client c : clients) {
            save(c);
        }
    }
    @Override
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }

    @Override
    @Transactional
    public void delete(Long id){
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Aucun enregistrement trouvé avec id = " + id);
        }
        repo.deleteById(id);
    }
    @Override
    public Client updateClient(Long id, Client payload) {
        Client existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        existing.setNom(payload.getNom());
        existing.setPrenom(payload.getPrenom());
        existing.setNumeroIdentite(payload.getNumeroIdentite());
        existing.setAdresse(payload.getAdresse());
        existing.setNationalite(payload.getNationalite());
        existing.setDateNaissance(payload.getDateNaissance());
        existing.setSexe(payload.getSexe());
        existing.setCommentaire(payload.getCommentaire());

        return repo.save(existing);
    }

    @Override
    public List<Client> searchClients(String searchTerm) {
        return repo.searchClients(searchTerm);
    }

    @Override
    public long getTotalClientsCount() {
        return repo.count();
    }

}
