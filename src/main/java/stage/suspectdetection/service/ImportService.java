package stage.suspectdetection.service;
import org.springframework.web.multipart.MultipartFile;

public interface ImportService {
    /**
     * Parse et sauvegarde les personnes sanctionnées dans la liste donnée.
     * @param file fichier Excel/CSV
     * @param listeId id de la liste de surveillance
     */
    void importSanctions(org.springframework.web.multipart.MultipartFile file, Long listeId) throws Exception;
}