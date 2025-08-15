package stage.suspectdetection.service;

import stage.suspectdetection.entities.Client;
import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    Client getById(String id);
    Client save(Client c);
}
