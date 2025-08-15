package stage.suspectdetection.service.impl;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.suspectdetection.entities.Client;
import stage.suspectdetection.entities.PersonneSanctionnee;
import stage.suspectdetection.service.MatchingService;
import stage.suspectdetection.service.ClientService;
import stage.suspectdetection.service.CasSuspectService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MatchingServiceImpl implements MatchingService {

    private final ClientService clientService;
    private final CasSuspectService casService;

    // Distance de Levenshtein pour comparer la similarité des chaînes
    private final LevenshteinDistance lev = new LevenshteinDistance();

    // Barème des poids : clé = nom du champ, valeur = tableau {equalWeight, emptyWeight, nonEqualWeight}
    private final Map<String, int[]> weights = new HashMap<>();

    public MatchingServiceImpl(ClientService clientService, CasSuspectService casService) {
        this.clientService = clientService;
        this.casService = casService;

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

    /**
     * Évalue tous les clients par rapport à une personne sanctionnée.
     * Si le score >= 85%, on enregistre un cas suspect.
     */
    @Override
    public void evaluateMatches(PersonneSanctionnee sanctionedPerson) {
        List<Client> clients = clientService.getAllClients();
        for (Client c : clients) {
            double score = calculateMatchScore(c, sanctionedPerson);
            if (score >= 0.85) { // Seuil configurable
                casService.saveSuspectCase(c, sanctionedPerson, score);
            }
        }
    }

    /**
     * Calcule le score global entre un client et une personne sanctionnée
     * en appliquant le barème et la similarité de Levenshtein.
     */
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
                client.getNationalite() == null ? "" : safe(client.getNationalite().getLibelle()),
                p.getNationalite() == null ? "" : safe(p.getNationalite().getLibelle()));
        totalPossible += weights.get("pays")[0];

        // Date de naissance
        totalScore += computeDateField(weights.get("dob"), client.getDateNaissance(), p.getDateNaissance());
        totalPossible += weights.get("dob")[0];

        // Retourne score normalisé (0 à 1)
        return totalScore / totalPossible;
    }

    /** Compare deux chaînes avec Levenshtein et pondère selon les poids */
    private double computeField(String key, String a, String b) {
        int[] w = weights.get(key);
        if (isEmpty(a) && isEmpty(b)) return w[1];
        if (!isEmpty(a) && !isEmpty(b) && a.equalsIgnoreCase(b)) return w[0];

        // Si différent : interpolation entre nonEqual et equal selon similarité
        double similarity = levenshteinSimilarity(a, b);
        return w[2] + (w[0] - w[2]) * similarity;
    }

    /** Compare simplement sans Levenshtein (exact match ou vide) */
    private double computeFieldSimple(int[] w, String a, String b) {
        if (isEmpty(a) && isEmpty(b)) return w[1];
        if (!isEmpty(a) && !isEmpty(b) && a.equalsIgnoreCase(b)) return w[0];
        return w[2];
    }

    /** Compare des dates avec poids */
    private double computeDateField(int[] w, LocalDate d1, LocalDate d2) {
        if (d1 == null && d2 == null) return w[1];
        if (d1 != null && d2 != null && d1.equals(d2)) return w[0];
        return w[2];
    }

    /** Calcul de similarité Levenshtein normalisée (0 à 1) */
    private double levenshteinSimilarity(String s1, String s2) {
        if (isEmpty(s1) || isEmpty(s2)) return 0.0;
        int max = Math.max(s1.length(), s2.length());
        int dist = lev.apply(s1.toLowerCase().trim(), s2.toLowerCase().trim());
        return Math.max(0, Math.min(1, 1.0 - ((double) dist / max)));
    }

    /** Helpers */
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String safe(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
