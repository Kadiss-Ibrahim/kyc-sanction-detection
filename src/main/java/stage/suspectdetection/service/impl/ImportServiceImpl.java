package stage.suspectdetection.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.ListeSurveillance;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.repositories.ClientRepository;
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
    private final ClientRepository clientRepo;

    public ImportServiceImpl(PersonneSanctionneeRepository personneRepo,
                             ListeSurveillanceRepository listeRepo,
                             ClientRepository clientRepo) {
        this.personneRepo = personneRepo;
        this.listeRepo = listeRepo;
        this.clientRepo = clientRepo;
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
                Long id = null;
                if (cellId != null) {
                    if (cellId.getCellType() == CellType.NUMERIC) {
                        id = (long) cellId.getNumericCellValue();
                    } else if (cellId.getCellType() == CellType.STRING) {
                        id = Long.parseLong(cellId.getStringCellValue().trim());
                    }
                }

                PersonneSanctionnee p;
                if (id != null) {
                    p = personneRepo.findById(id).orElse(new PersonneSanctionnee());
                    p.setId(id); // important pour la persistance
                } else {
                    p = new PersonneSanctionnee();
                }

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

    @Override
    public void importClients(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                Long id = parseId(row.getCell(0));

                Client client;
                if (id != null) {
                    client = clientRepo.findById(id).orElse(new Client());
                    client.setId(id);
                } else {
                    client = new Client();
                }

                client.setNom(getCellString(row.getCell(1)));
                client.setPrenom(getCellString(row.getCell(2)));
                client.setNumeroIdentite(getCellString(row.getCell(3)));

                Cell dateCell = row.getCell(4);
                if (dateCell != null && DateUtil.isCellDateFormatted(dateCell)) {
                    Date date = dateCell.getDateCellValue();
                    client.setDateNaissance(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }

                clientRepo.save(client);
            }
        }
    }
    private String getCellString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long)cell.getNumericCellValue());
        return cell.toString().trim();
    }

    private Long parseId(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (long) cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) return Long.parseLong(cell.getStringCellValue().trim());
        return null;
    }
}
