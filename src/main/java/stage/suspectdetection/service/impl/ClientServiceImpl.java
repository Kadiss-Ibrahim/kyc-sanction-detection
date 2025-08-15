package stage.suspectdetection.service.impl;

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
    public Client getById(String id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Client save(Client c) {
        return repo.save(c);
    }
}
