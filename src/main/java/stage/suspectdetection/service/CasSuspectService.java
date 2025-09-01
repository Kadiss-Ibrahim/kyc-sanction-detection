package stage.suspectdetection.service;

import stage.suspectdetection.entities.CasSuspect;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.PersonneSanctionnee;
import java.util.List;

public interface CasSuspectService {
    CasSuspect saveSuspectCase(Client client, PersonneSanctionnee sanctionedPerson, double score);
    List<CasSuspect> getAllCases();
    List<CasSuspect> getCasesByClientId(Long clientId);
    CasSuspect updateStatus(Long id, String statut); // statut -> map sur Enum si besoin
    void delete(Long id);
    List<CasSuspect> searchCasSuspects(String searchTerm);
    long getTotalCasSuspectsCount();
}
