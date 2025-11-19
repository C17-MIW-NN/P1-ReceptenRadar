package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Iris Loermans
 */
public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
