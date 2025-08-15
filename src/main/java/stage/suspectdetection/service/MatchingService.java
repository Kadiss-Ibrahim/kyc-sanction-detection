package stage.suspectdetection.service;


import stage.suspectdetection.entities.PersonneSanctionnee;

public interface MatchingService {
    /**
     * Evalue les correspondances entre la personne sanctionnée et la base clients.
     * S'occupe d'appeler CasSuspectService pour enregistrer les cas supérieurs au seuil.
     */
    void evaluateMatches(PersonneSanctionnee sanctionedPerson);
}
