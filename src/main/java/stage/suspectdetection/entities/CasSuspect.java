package stage.suspectdetection.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import stage.suspectdetection.enums.EtatCasSuspect;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //  on inclut uniquement l'id
public class CasSuspect extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate dateDetection;
    private Double scoreSimilaritee;

    @Enumerated(EnumType.STRING)
    private EtatCasSuspect etat;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JoinColumn(name = "personne_sanctionnee_id")
    @JsonIgnore
    private PersonneSanctionnee personneSanctionnee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDetection() {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection) {
        this.dateDetection = dateDetection;
    }

    public Double getScoreSimilaritee() {
        return scoreSimilaritee;
    }

    public void setScoreSimilaritee(Double scoreSimilaritee) {
        this.scoreSimilaritee = scoreSimilaritee;
    }

    public EtatCasSuspect getEtat() {
        return etat;
    }

    public void setEtat(EtatCasSuspect etat) {
        this.etat = etat;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PersonneSanctionnee getPersonneSanctionnee() {
        return personneSanctionnee;
    }

    public void setPersonneSanctionnee(PersonneSanctionnee personneSanctionnee) {
        this.personneSanctionnee = personneSanctionnee;
    }
}
