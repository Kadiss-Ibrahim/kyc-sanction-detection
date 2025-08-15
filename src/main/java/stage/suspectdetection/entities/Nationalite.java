package stage.suspectdetection.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Nationalite {

    @Id
    @Column(length = 10)
    private String codeLibelle;  // ex: "MAR" pour Maroc

    private String libelle;      // ex: "Maroc"

    public String getCodeLibelle() {
        return codeLibelle;
    }

    public void setCodeLibelle(String codeLibelle) {
        this.codeLibelle = codeLibelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}