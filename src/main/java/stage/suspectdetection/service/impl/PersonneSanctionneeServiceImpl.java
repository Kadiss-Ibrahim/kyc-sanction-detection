package stage.suspectdetection.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
    public PersonneSanctionnee getById(Long id) {
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
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Aucun enregistrement trouvé avec id = " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public boolean isSanctionnee(Long id) {
        return repo.existsById(id);
    }

    @Override
    public void updateSanctionnees(List<Long> idsSanctionnes) {
        List<PersonneSanctionnee> toutes = repo.findAll();
        for (PersonneSanctionnee p : toutes) {
            boolean est = idsSanctionnes.contains(p.getId());
            if (p.isSanctionnee() != est) {
                p.setSanctionnee(est);
                repo.save(p);
            }
        }
    }
    @Override
    @Transactional
    public void saveAll(List<PersonneSanctionnee> personnes) {
        for (PersonneSanctionnee p : personnes) {
            repo.save(p);
        }
    }
    @Override
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }
}
