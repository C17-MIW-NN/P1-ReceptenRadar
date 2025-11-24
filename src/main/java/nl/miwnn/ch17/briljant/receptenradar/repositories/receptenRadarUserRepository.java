package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.receptenRadarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Douwe Jan Hamersma
 */

public interface receptenRadarUserRepository extends JpaRepository<receptenRadarUser, Long> {
    Optional<receptenRadarUser> findByUserName(String userName);


}
