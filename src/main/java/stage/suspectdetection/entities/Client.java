package stage.suspectdetection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private Set<CasSuspect> casSuspects;

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
