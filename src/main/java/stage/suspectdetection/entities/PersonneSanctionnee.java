package stage.suspectdetection.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonneSanctionnee extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Pour les personnes supprimer de la liste
    private boolean sanctionnee = true;

    @ManyToOne
    @JoinColumn(name = "liste_surveillance_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private ListeSurveillance listeSurveillance;

    private LocalDateTime dateCreation;


    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now(); // ⬅auto initialisation
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateMiseAJour) {
        this.dateCreation = dateMiseAJour;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSanctionnee() {
        return sanctionnee;
    }

    public void setSanctionnee(boolean sanctionnee) {
        this.sanctionnee = sanctionnee;
    }

    public ListeSurveillance getListeSurveillance() {
        return listeSurveillance;
    }

    public void setListeSurveillance(ListeSurveillance listeSurveillance) {
        this.listeSurveillance = listeSurveillance;
    }
}
