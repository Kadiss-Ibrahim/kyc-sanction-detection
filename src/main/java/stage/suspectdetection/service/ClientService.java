package stage.suspectdetection.service;

import stage.suspectdetection.entities.Client;
import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    Client getById(Long id);
    Client save(Client c);
    void saveAll(List<Client> clients);
    void deleteAll();
    void delete(Long id);
}
