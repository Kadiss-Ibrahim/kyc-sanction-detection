package stage.suspectdetection.service;

import stage.suspectdetection.entities.ListeSurveillance;
import java.util.List;

public interface ListeSurveillanceService {
    ListeSurveillance createList(String nom, String source);
    ListeSurveillance getById(Long id);
    List<ListeSurveillance> getAll();
    void delete(Long id);
    ListeSurveillance update(Long id, String nomListe, String source);
    List<ListeSurveillance> searchListesSurveillance(String searchTerm);
    long getTotalListesSurveillanceCount();
}
