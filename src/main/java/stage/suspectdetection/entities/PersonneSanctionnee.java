package stage.suspectdetection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonneSanctionnee extends Personne {
    @Id
    private String id;
    //Pour les personnes supprimer de la liste
    private boolean sanctionnee;

    @ManyToOne
    @JoinColumn(name = "liste_surveillance_id")
    private ListeSurveillance listeSurveillance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
