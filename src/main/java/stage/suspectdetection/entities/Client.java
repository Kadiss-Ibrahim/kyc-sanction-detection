package stage.suspectdetection.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="client")
public class Client extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        // Si on supprime un client on debarasse les cas suspects lie
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CasSuspect> casSuspects;

    private LocalDateTime dateMiseAJour;

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CasSuspect> getCasSuspects() {
        return casSuspects;
    }

    public void setCasSuspects(Set<CasSuspect> casSuspects) {
        this.casSuspects = casSuspects;
    }

    //Constructeur pour test

}
