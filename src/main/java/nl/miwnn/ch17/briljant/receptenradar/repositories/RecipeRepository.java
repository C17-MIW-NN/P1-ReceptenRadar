package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Johan Elzinga
 */

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);
}
