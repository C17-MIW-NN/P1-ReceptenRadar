package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Johan Elzinga
 */

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
