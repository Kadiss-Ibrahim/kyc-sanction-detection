package stage.suspectdetection.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.ListeSurveillance;
import stage.suspectdetection.repositories.ListeSurveillanceRepository;
import stage.suspectdetection.service.ListeSurveillanceService;

import java.util.List;

@Service
@Transactional
public class ListeSurveillanceServiceImpl implements ListeSurveillanceService {

    private final ListeSurveillanceRepository repo;

    public ListeSurveillanceServiceImpl(ListeSurveillanceRepository repo) {
        this.repo = repo;
    }

    @Override
    public ListeSurveillance createList(String nom, String source) {
        ListeSurveillance l = new ListeSurveillance();
        l.setNomListe(nom);
        l.setSource(source);
        return repo.save(l);
    }

    @Override
    public ListeSurveillance getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<ListeSurveillance> getAll() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public ListeSurveillance update(Long id, String nomListe, String source) {
        ListeSurveillance liste = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Liste introuvable avec id = " + id));
        if (nomListe != null && !nomListe.isEmpty()) {
            liste.setNomListe(nomListe);
        }
        if (source != null && !source.isEmpty()) {
            liste.setSource(source);
        }
        return repo.save(liste);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Aucun enregistrement trouvé avec id = " + id);
        }
        repo.deleteById(id);
    }
}
