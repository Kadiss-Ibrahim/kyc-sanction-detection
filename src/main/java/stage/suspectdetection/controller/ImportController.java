package stage.suspectdetection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stage.suspectdetection.service.ImportService;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/sanctions/{listeId}")
    public ResponseEntity<String> importSanctions(
            @PathVariable Long listeId,
            @RequestParam("file") MultipartFile file) {
        try {
            importService.importSanctions(file, listeId);
            return ResponseEntity.ok("Importation des sanctions réussie !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'importation : " + e.getMessage());
        }
    }

    @PostMapping("/clients")
    public ResponseEntity<String> importClients(
            @RequestParam("file") MultipartFile file) {
        try {
            importService.importClients(file);
            return ResponseEntity.ok("Importation des clients réussie !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'importation : " + e.getMessage());
        }
    }
}
