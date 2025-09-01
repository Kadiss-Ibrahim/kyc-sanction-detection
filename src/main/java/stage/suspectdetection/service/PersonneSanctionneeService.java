package stage.suspectdetection.service;

import stage.suspectdetection.entities.PersonneSanctionnee;
import java.util.List;

public interface PersonneSanctionneeService {
    PersonneSanctionnee getById(Long id);
    List<PersonneSanctionnee> getAll();
    PersonneSanctionnee save(PersonneSanctionnee p);
    void delete(Long id);
    boolean isSanctionnee(Long id);
    PersonneSanctionnee updatePersonne(Long id, PersonneSanctionnee updatedPersonne);
    void saveAll(List<PersonneSanctionnee> personnes);
    List<PersonneSanctionnee> getByListe(Long listeId);
    void deleteAll();
    List<PersonneSanctionnee> searchPersonnesSanctionnees(String searchTerm);
    long getTotalPersonnesSanctionneesCount();
}
