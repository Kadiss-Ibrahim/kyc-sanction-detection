package stage.suspectdetection.service;

import stage.suspectdetection.entities.PersonneSanctionnee;
import java.util.List;

public interface PersonneSanctionneeService {
    PersonneSanctionnee getById(Long id);
    List<PersonneSanctionnee> getAll();
    PersonneSanctionnee save(PersonneSanctionnee p);
    void delete(Long id);
    boolean isSanctionnee(Long id);
    void updateSanctionnees(java.util.List<Long> idsSanctionnes);
    void saveAll(List<PersonneSanctionnee> personnes);

    void deleteAll();
}
