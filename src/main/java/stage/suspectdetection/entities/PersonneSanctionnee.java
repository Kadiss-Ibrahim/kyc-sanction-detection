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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Pour les personnes supprimer de la liste
    private boolean sanctionnee;

    @ManyToOne
    @JoinColumn(name = "liste_surveillance_id")
    private ListeSurveillance listeSurveillance;

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
