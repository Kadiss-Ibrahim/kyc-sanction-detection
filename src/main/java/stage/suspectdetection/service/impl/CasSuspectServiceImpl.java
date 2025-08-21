package stage.suspectdetection.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.CasSuspect;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.enums.EtatCasSuspect;
import stage.suspectdetection.repositories.CasSuspectRepository;
import stage.suspectdetection.service.CasSuspectService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CasSuspectServiceImpl implements CasSuspectService {

    private final CasSuspectRepository repo;

    public CasSuspectServiceImpl(CasSuspectRepository repo) {
        this.repo = repo;
    }

    @Override
    public CasSuspect saveSuspectCase(Client client, PersonneSanctionnee sp, double score) {
        CasSuspect cs = new CasSuspect();
        cs.setDateDetection(LocalDate.now());
        cs.setScoreSimilaritee(score);
        // Par défaut, tous les nouveaux cas sont à vérifier
        cs.setEtat(EtatCasSuspect.NON_VERIFIE);

        cs.setClient(client);
        cs.setPersonneSanctionnee(sp);

        cs.setNom(client.getNom());
        cs.setPrenom(client.getPrenom());
        cs.setAdresse(client.getAdresse());
        cs.setNationalite(client.getNationalite());
        cs.setNumeroIdentite(client.getNumeroIdentite());
        cs.setSexe(client.getSexe());


        return repo.save(cs);
    }

    @Override
    public List<CasSuspect> getAllCases() {
        return repo.findAll();
    }

    @Override
    public List<CasSuspect> getCasesByClientId(Long clientId) {
        return repo.findByClientId(clientId);
    }

    @Override
    public CasSuspect updateStatus(Long id, String statut) {
        CasSuspect cs = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Case not found"));
        cs.setEtat(EtatCasSuspect.valueOf(statut));
        return repo.save(cs);
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
