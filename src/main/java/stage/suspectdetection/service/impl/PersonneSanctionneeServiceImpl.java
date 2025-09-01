package stage.suspectdetection.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.ListeSurveillance;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.repositories.ListeSurveillanceRepository;
import stage.suspectdetection.repositories.PersonneSanctionneeRepository;
import stage.suspectdetection.service.PersonneSanctionneeService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PersonneSanctionneeServiceImpl implements PersonneSanctionneeService {

    private final PersonneSanctionneeRepository repo;
    private final ListeSurveillanceRepository listeSurveillanceRepository; // AJOUT

    public PersonneSanctionneeServiceImpl(PersonneSanctionneeRepository repo,
                                          ListeSurveillanceRepository listeSurveillanceRepository) {
        this.repo = repo;
        this.listeSurveillanceRepository = listeSurveillanceRepository; // AJOUT
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
        if (p.getListeSurveillance() != null && p.getListeSurveillance().getId() != null) {
            ListeSurveillance liste = listeSurveillanceRepository.findById(p.getListeSurveillance().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Liste de surveillance non trouvée"));
            p.setListeSurveillance(liste);
        }
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
    public PersonneSanctionnee updatePersonne(Long id, PersonneSanctionnee updatedPersonne) {
        PersonneSanctionnee existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Personne non trouvée"));

        existing.setNom(updatedPersonne.getNom());
        existing.setPrenom(updatedPersonne.getPrenom());
        existing.setDateNaissance(updatedPersonne.getDateNaissance());
        existing.setSexe(updatedPersonne.getSexe());
        existing.setAdresse(updatedPersonne.getAdresse());
        existing.setCommentaire(updatedPersonne.getCommentaire());
        existing.setSanctionnee(updatedPersonne.isSanctionnee());

        if (updatedPersonne.getListeSurveillance() != null && updatedPersonne.getListeSurveillance().getId() != null) {
            ListeSurveillance liste = listeSurveillanceRepository.findById(updatedPersonne.getListeSurveillance().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Liste de surveillance non trouvée"));
            existing.setListeSurveillance(liste);
        }

        return repo.save(existing);
    }

    @Override
    @Transactional
    public void saveAll(List<PersonneSanctionnee> personnes) {
        repo.saveAll(personnes);
    }

    @Override
    public List<PersonneSanctionnee> getByListe(Long listeId) {
        // Vérifier que la liste existe
        if (!listeSurveillanceRepository.existsById(listeId)) {
            throw new EntityNotFoundException("Liste de surveillance introuvable avec id = " + listeId);
        }
        return repo.findByListeSurveillanceId(listeId);
    }

    @Override
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }

    @Override
    public List<PersonneSanctionnee> searchPersonnesSanctionnees(String searchTerm) {
        return repo.searchPersonnesSanctionnees(searchTerm);
    }

    @Override
    public long getTotalPersonnesSanctionneesCount() {
        return repo.count();
    }

}
