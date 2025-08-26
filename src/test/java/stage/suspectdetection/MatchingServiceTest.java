package stage.suspectdetection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.Notification;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.enums.Sexe;
import stage.suspectdetection.service.NotificationService;
import stage.suspectdetection.service.impl.MatchingServiceImpl;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Transactional
//@Commit
public class MatchingServiceTest {
    @Autowired
    private MatchingServiceImpl matchingService;

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    public void setup() {
        // Nettoyer la DB avant chaque test
        matchingService.getClientService().deleteAll();
        matchingService.getSanctionneeService().deleteAll();
    }

    @Test
    public void testEvaluateAllMatches_singleMatch() {
        // Clients
        Client client1 = new Client();
        client1.setNom("Ibrahim");
        client1.setPrenom("Kadiss");
        client1.setSexe(Sexe.MASCULIN);
        client1.setNumeroIdentite("123456");

        Client client2 = new Client();
        client2.setNom("Youssef");
        client2.setPrenom("Ait-Kadiss");
        client2.setSexe(Sexe.MASCULIN);
        client2.setNumeroIdentite("654321");

// Personnes sanctionnées fictives
        PersonneSanctionnee ps1 = new PersonneSanctionnee();
        ps1.setNom("Ibrahim");
        ps1.setPrenom("Kadiss");
        ps1.setSexe(Sexe.MASCULIN);
        ps1.setNumeroIdentite("123456");

        matchingService.getClientService().saveAll(Arrays.asList(client1, client2));
        matchingService.getSanctionneeService().save(ps1);

        // Évaluer
        matchingService.evaluateAllMatches();

        Notification lastNotif = notificationService.getLastNotification();
        assertEquals("1 cas suspects détectés lors de la dernière actualisation.", lastNotif.getMessage());
    }

    @Test
    public void testEvaluateAllMatches_multipleMatches() {
        Client client1 = new Client();
        client1.setNom("Ibrahim");
        client1.setPrenom("Kadiss");
        client1.setSexe(Sexe.MASCULIN);
        client1.setNumeroIdentite("123456");
        client1.setNationalite("Mroc");


        Client client2 = new Client();
        client2.setNom("Youssef");
        client2.setPrenom("Ait-Kadiss");
        client2.setSexe(Sexe.MASCULIN);
        client2.setNumeroIdentite("654321");

        Client client3 = new Client();
        client3.setNom("Mouad");
        client3.setPrenom("Kadsos");
        client3.setSexe(Sexe.MASCULIN);
        client3.setNumeroIdentite("7654");
        client3.setAdresse("sidi maarouf");
        client3.setCommentaire("khatar");

        // Personnes sanctionnées fictives
        PersonneSanctionnee ps1 = new PersonneSanctionnee();
        ps1.setNom("Ibrahim");
        ps1.setPrenom("Kadiss");
        ps1.setSexe(Sexe.MASCULIN);
        ps1.setNumeroIdentite("123456");
        ps1.setAdresse("sidi maarouf");
        ps1.setCommentaire("khataR");
        ps1.setNationalite("Maroc");

        PersonneSanctionnee ps2 = new PersonneSanctionnee();
        ps2.setNom("Youss");
        ps2.setPrenom("Ait-Kadiss");
        ps2.setSexe(Sexe.FEMININ);
        ps2.setNumeroIdentite("654321");

        PersonneSanctionnee ps3 = new PersonneSanctionnee();
        ps3.setNom("Mouad");
        ps3.setPrenom("Kadss");
        ps3.setSexe(Sexe.MASCULIN);
        ps3.setNumeroIdentite("987654");
        ps3.setAdresse("sidi maarouf");
        ps3.setCommentaire("khataR");

        matchingService.getClientService().saveAll(Arrays.asList(client1, client2, client3));
        matchingService.getSanctionneeService().saveAll(Arrays.asList(ps1, ps2, ps3));

        // Évaluer
        matchingService.evaluateAllMatches();

        Notification lastNotif = notificationService.getLastNotification();
        assertEquals("3 cas suspects détectés lors de la dernière actualisation.", lastNotif.getMessage());
    }

    @Test
    public void testEvaluateAllMatches_noMatches() {
        Client client1 = new Client();
        client1.setNom("Ibrahim");
        client1.setPrenom("Kadiss");
        client1.setSexe(Sexe.MASCULIN);
        client1.setNumeroIdentite("123456");


        PersonneSanctionnee ps2 = new PersonneSanctionnee();
        ps2.setNom("John");
        ps2.setPrenom("Doe");
        ps2.setSexe(Sexe.MASCULIN);
        ps2.setNumeroIdentite("987654");
        matchingService.getClientService().save(client1);
        matchingService.getSanctionneeService().save(ps2);

        // Évaluer
        matchingService.evaluateAllMatches();

        Notification lastNotif = notificationService.getLastNotification();
        assertEquals("0 cas suspects détectés lors de la dernière actualisation.", lastNotif.getMessage());
    }

    @Test
    public void testEvaluateAllMatches_emptyDatabase() {
        // Pas de clients ni de sanctionnés
        matchingService.evaluateAllMatches();

        Notification lastNotif = notificationService.getLastNotification();
        assertEquals("0 cas suspects détectés lors de la dernière actualisation.", lastNotif.getMessage());
    }
}