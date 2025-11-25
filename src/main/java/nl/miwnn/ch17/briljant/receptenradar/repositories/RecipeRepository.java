package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Johan Elzinga
 */

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeId(Long recipeId);
    Optional<Recipe> findByRecipeName(String recipeName);
    List<Recipe> findByCategories_CategoryName(String categoryName);

}
