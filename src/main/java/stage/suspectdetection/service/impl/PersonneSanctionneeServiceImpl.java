package stage.suspectdetection.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.repositories.PersonneSanctionneeRepository;
import stage.suspectdetection.service.PersonneSanctionneeService;

import java.util.List;

@Service
@Transactional
public class PersonneSanctionneeServiceImpl implements PersonneSanctionneeService {

    private final PersonneSanctionneeRepository repo;

    public PersonneSanctionneeServiceImpl(PersonneSanctionneeRepository repo) {
        this.repo = repo;
    }

    @Override
    public PersonneSanctionnee getById(String id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<PersonneSanctionnee> getAll() {
        return repo.findAll();
    }

    @Override
    public PersonneSanctionnee save(PersonneSanctionnee p) {
        return repo.save(p);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }

    @Override
    public boolean isSanctionnee(String id) {
        return repo.existsById(id);
    }

    @Override
    public void updateSanctionnees(List<String> idsSanctionnes) {
        List<PersonneSanctionnee> toutes = repo.findAll();
        for (PersonneSanctionnee p : toutes) {
            boolean est = idsSanctionnes.contains(p.getId());
            if (p.isSanctionnee() != est) {
                p.setSanctionnee(est);
                repo.save(p);
            }
        }
    }
}
