package stage.suspectdetection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListeSurveillance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomListe;
    private String source;

    @OneToMany(mappedBy = "listeSurveillance", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonneSanctionnee> personnes;

    private LocalDate dateCreation;

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<PersonneSanctionnee> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(Set<PersonneSanctionnee> personnes) {
        this.personnes = personnes;
    }
}
