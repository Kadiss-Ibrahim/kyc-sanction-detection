package stage.suspectdetection.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import stage.suspectdetection.entities.ListeSurveillance;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.repositories.ListeSurveillanceRepository;
import stage.suspectdetection.repositories.PersonneSanctionneeRepository;
import stage.suspectdetection.service.ImportService;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class ImportServiceImpl implements ImportService {

    private final PersonneSanctionneeRepository personneRepo;
    private final ListeSurveillanceRepository listeRepo;

    public ImportServiceImpl(PersonneSanctionneeRepository personneRepo,
                             ListeSurveillanceRepository listeRepo) {
        this.personneRepo = personneRepo;
        this.listeRepo = listeRepo;
    }

    @Override
    public void importSanctions(MultipartFile file, Long listeId) throws Exception {
        ListeSurveillance liste = listeRepo.findById(listeId)
                .orElseThrow(() -> new IllegalArgumentException("Liste introuvable"));

        Set<PersonneSanctionnee> saved = new HashSet<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                // Exemple d'assignation de colonnes :
                // 0 = id, 1 = nom, 2 = prenom, 3 = numeroIdentite, 4 = dateNaissance
                Cell cellId = row.getCell(0);
                String id = getCellString(cellId);

                PersonneSanctionnee p = personneRepo.findById(id).orElse(new PersonneSanctionnee());
                p.setId(id);
                p.setNom(getCellString(row.getCell(1)));
                p.setPrenom(getCellString(row.getCell(2)));
                p.setNumeroIdentite(getCellString(row.getCell(3)));

                Cell dateCell = row.getCell(4);
                if (dateCell != null && DateUtil.isCellDateFormatted(dateCell)) {
                    Date date = dateCell.getDateCellValue();
                    p.setDateNaissance(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }

                p.setSanctionnee(true);
                p.setListeSurveillance(liste);
                personneRepo.save(p);
                saved.add(p);
            }
        }
        // Optionnel : attacher la collection à la liste
        liste.setPersonnes(saved);
        listeRepo.save(liste);
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long)cell.getNumericCellValue());
        return cell.toString().trim();
    }
}
