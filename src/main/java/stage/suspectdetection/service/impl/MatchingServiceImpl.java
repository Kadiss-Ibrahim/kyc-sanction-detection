package stage.suspectdetection.service.impl;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.entities.Utilisateur;
import stage.suspectdetection.service.*;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MatchingServiceImpl implements MatchingService {

    private final ClientService clientService;
    private final CasSuspectService casService;
    private final PersonneSanctionneeService sanctionneeService;
    private final NotificationService notificationService;
    private final LevenshteinDistance lev = new LevenshteinDistance();

    // Barème des poids : clé = nom du champ, valeur = tableau {equalWeight, emptyWeight, nonEqualWeight}
    private final Map<String, int[]> weights = new HashMap<>();

    public MatchingServiceImpl(ClientService clientService,
                               CasSuspectService casService,
                               PersonneSanctionneeService sanctionneeService,
                               NotificationService notificationService) {
        this.clientService = clientService;
        this.casService = casService;
        this.sanctionneeService = sanctionneeService;
        this.notificationService = notificationService;

        // Initialisation des poids selon le tableau fourni
        weights.put("pertinent1", new int[]{75, 15, 0});
        weights.put("pertinent2", new int[]{50, 35, 0});
        weights.put("pertinent3", new int[]{15, 14, 7});
        weights.put("pertinent4", new int[]{10, 9, 5});
        weights.put("pertinent5", new int[]{10, 9, 5});
        weights.put("pertinent6", new int[]{5, 4, 2});

        weights.put("sexe", new int[]{20, 20, 0});
        weights.put("pays", new int[]{20, 20, 0});
        weights.put("dob", new int[]{20, 20, 0});
    }
    @Override
    public void evaluateAllMatches() {
        List<PersonneSanctionnee> sanctionnees = sanctionneeService.getAll();
        int totalCasDetectes = 0;

        for (PersonneSanctionnee p : sanctionnees) {
            totalCasDetectes += evaluateMatches(p);
        }

        // 🔹 Après avoir évalué tout le monde, mettre à jour la dateMiseAJour
        List<Client> clients = clientService.getAllClients();
        LocalDateTime now = LocalDateTime.now();
        for (Client c : clients) {
            c.setDateMiseAJour(now);
            clientService.save(c); // persister la mise à jour
        }

        // 🔔 Envoyer notification
        String msg = totalCasDetectes + " cas suspects détectés lors de la dernière actualisation.";
        notificationService.envoyerNotification(msg);
    }

    @Override
    public int evaluateMatches(PersonneSanctionnee sanctionedPerson) {
        List<Client> clients = clientService.getAllClients();
        int nbCasDetectes = 0;

        for (Client c : clients) {
            // 🔹 Ne traiter que si le client n'a pas encore été mis à jour
            if (c.getDateMiseAJour() == null || c.getDateMiseAJour().isBefore(sanctionedPerson.getDateCreation())) {
                double score = calculateMatchScore(c, sanctionedPerson);
                if (score >= 0.85) {
                    nbCasDetectes++;
                    casService.saveSuspectCase(c, sanctionedPerson, score);
                }
            }
        }
        return nbCasDetectes;
    }

    private double calculateMatchScore(Client client, PersonneSanctionnee p) {
        double totalPossible = 0.0;
        double totalScore = 0.0;

        // Mapping des champs aux "pertinents"
        totalScore += computeField("pertinent1", safe(client.getNumeroIdentite()), safe(p.getNumeroIdentite()));
        totalPossible += weights.get("pertinent1")[0];

        totalScore += computeField("pertinent2", safe(client.getNom()) + " " + safe(client.getPrenom()),
                safe(p.getNom()) + " " + safe(p.getPrenom()));
        totalPossible += weights.get("pertinent2")[0];

        totalScore += computeField("pertinent3", safe(client.getNom()), safe(p.getNom()));
        totalPossible += weights.get("pertinent3")[0];

        totalScore += computeField("pertinent4", safe(client.getPrenom()), safe(p.getPrenom()));
        totalPossible += weights.get("pertinent4")[0];

        totalScore += computeField("pertinent5", safe(client.getAdresse()), safe(p.getAdresse()));
        totalPossible += weights.get("pertinent5")[0];

        totalScore += computeField("pertinent6", safe(client.getCommentaire()), safe(p.getCommentaire()));
        totalPossible += weights.get("pertinent6")[0];

        // Sexe
        totalScore += computeFieldSimple(weights.get("sexe"),
                client.getSexe() == null ? "" : client.getSexe().name(),
                p.getSexe() == null ? "" : p.getSexe().name());
        totalPossible += weights.get("sexe")[0];

        // Pays
        totalScore += computeFieldSimple(weights.get("pays"),
                client.getNationalite() == null ? "" : safe(client.getNationalite()),
                p.getNationalite() == null ? "" : safe(p.getNationalite()));
        totalPossible += weights.get("pays")[0];

        // Date de naissance
        totalScore += computeDateField(weights.get("dob"), client.getDateNaissance(), p.getDateNaissance());
        totalPossible += weights.get("dob")[0];

        // Retourne score normalisé (0 à 1)
        return totalScore / totalPossible;
    }

    private double computeField(String key, String a, String b) {
        int[] w = weights.get(key);
        if (isEmpty(a) && isEmpty(b)) return w[1];
        if (!isEmpty(a) && !isEmpty(b) && a.equalsIgnoreCase(b)) return w[0];

        // Si différent : interpolation entre nonEqual et equal selon similarité
        double similarity = levenshteinSimilarity(a, b);
        return w[2] + (w[0] - w[2]) * similarity;
    }

    private double computeFieldSimple(int[] w, String a, String b) {
        if (isEmpty(a) && isEmpty(b)) return w[1];
        if (!isEmpty(a) && !isEmpty(b) && a.equalsIgnoreCase(b)) return w[0];
        return w[2];
    }

    private double computeDateField(int[] w, LocalDate d1, LocalDate d2) {
        if (d1 == null && d2 == null) return w[1];
        if (d1 != null && d2 != null && d1.equals(d2)) return w[0];
        return w[2];
    }

    private double levenshteinSimilarity(String s1, String s2) {
        if (isEmpty(s1) || isEmpty(s2)) return 0.0;
        int max = Math.max(s1.length(), s2.length());
        int dist = lev.apply(safe(s1), safe(s2));
        return Math.max(0, Math.min(1, 1.0 - ((double) dist / max)));
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String safe(String s) {
        if (s == null) return "";

        // Supprimer les espaces début/fin et mettre en minuscule
        String result = s.trim().toLowerCase();

        // Normaliser et supprimer les accents
        result = Normalizer.normalize(result, Normalizer.Form.NFD);
        result = result.replaceAll("\\p{M}", ""); // enlève les diacritiques

        // Supprimer tout caractère non alphabétique (optionnel selon ton besoin)
        result = result.replaceAll("[^a-z0-9 ]", "");

        // Remplacer les espaces multiples par un seul
        result = result.replaceAll("\\s+", " ");

        return result;
    }

//Pour test
    public ClientService getClientService() {
        return this.clientService;
    }

    @Override
    public PersonneSanctionneeService getSanctionneeService() {
        return this.sanctionneeService;
    }

}
