package stage.suspectdetection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stage.suspectdetection.enums.Sexe;

import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Personne {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String adresse;
    private String commentaire;
    private Sexe sexe;

    @Column(unique = true)
    private String numeroIdentite;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationalite_code_libelle", referencedColumnName = "codeLibelle")
    private Nationalite nationalite;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getNumeroIdentite() {
        return numeroIdentite;
    }

    public void setNumeroIdentite(String numeroIdentite) {
        this.numeroIdentite = numeroIdentite;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }
}
