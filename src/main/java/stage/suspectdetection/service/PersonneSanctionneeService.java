package stage.suspectdetection.service;

import stage.suspectdetection.entities.PersonneSanctionnee;
import java.util.List;

public interface PersonneSanctionneeService {
    PersonneSanctionnee getById(String id);
    List<PersonneSanctionnee> getAll();
    PersonneSanctionnee save(PersonneSanctionnee p);
    void delete(String id);
    boolean isSanctionnee(String id);
    void updateSanctionnees(java.util.List<String> idsSanctionnes);
}
