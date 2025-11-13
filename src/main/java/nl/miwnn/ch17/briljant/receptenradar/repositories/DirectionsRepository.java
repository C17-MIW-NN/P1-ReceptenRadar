package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Directions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Iris Loermans
 */

@Repository
public interface DirectionsRepository extends JpaRepository<Directions,Long> {
}
