package stage.suspectdetection.service;


import stage.suspectdetection.entities.PersonneSanctionnee;


public interface MatchingService {
    void evaluateAllMatches();
    int evaluateMatches(PersonneSanctionnee sanctionedPerson);
    PersonneSanctionneeService getSanctionneeService() ;
}
