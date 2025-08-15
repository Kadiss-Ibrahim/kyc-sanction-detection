package stage.suspectdetection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.suspectdetection.entities.Utilisateur;
import org.springframework.stereotype.Repository;
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {}
