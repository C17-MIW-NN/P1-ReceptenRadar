package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.ReceptenRadarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Douwe Jan Hamersma
 */

public interface ReceptenRadarUserRepository extends JpaRepository<ReceptenRadarUser, Long> {
    Optional<ReceptenRadarUser> findByUsername(String username);

    String username(String username);
}
