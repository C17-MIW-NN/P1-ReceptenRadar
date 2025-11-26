package nl.miwnn.ch17.briljant.receptenradar.repositoryTests;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

/**
 * @author Douwe Jan Hamersma
 * junit tests for recipeRepository
 */

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class RecipeRepositoryTests {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void recipeRepository_saveAll_returnsSavedRecipe() {
        //Arrange
        Recipe recipe = Recipe.builder().recipeName("testRecept").build();

        //Act
        Recipe savedRecipe = recipeRepository.save(recipe);

        //Assert
        Assertions.assertNotNull(savedRecipe);
        Assertions.assertEquals(savedRecipe.getRecipeId(), recipe.getRecipeId());
        Assertions.assertEquals(savedRecipe.getRecipeName(), recipe.getRecipeName());

    }

    @Test
    public void recipeRepository_FindAll_returnsMoreThenOneRecipes() {
        //Arrange
        Recipe recipe = Recipe.builder().recipeName("testRecept").build();
        Recipe recipe2 = Recipe.builder().recipeName("testRecept2").build();

        //Act
        recipeRepository.save(recipe);
        recipeRepository.save(recipe2);

        List <Recipe> allRecipes = recipeRepository.findAll();

        //Assert
        Assertions.assertNotNull(allRecipes);
        Assertions.assertEquals(allRecipes.size(), 2);

    }

    @Test
    public void recipeRepository_FindById_returnsRecipe() {
        //Arrange
        Recipe recipe = Recipe.builder().recipeName("testRecept").build();

        //Act
        recipeRepository.save(recipe);

        Optional <Recipe> recipeToFind = recipeRepository.findById(recipe.getRecipeId());

        //Assert
        Assertions.assertTrue(recipeToFind.isPresent());
        Assertions.assertEquals("testRecept", recipeToFind.get().getRecipeName());
    }

    @Test
    public void recipeRepository_findByCategory_categoryName_returnsRecipe() {
        //Arrange
        Category italiaans = Category.builder().categoryName("Italiaans").build();
        Category vegetarisch = Category.builder().categoryName("Vegetarisch").build();
        Category dinner = Category.builder().categoryName("Dinner").build();

        categoryRepository.saveAll(List.of(italiaans, vegetarisch, dinner));

        Recipe recipe1 = Recipe.builder()
                .recipeName("Italiaanse Pasta")
                .categories(List.of(italiaans, vegetarisch, dinner))
                .build();

        Recipe recipe2 = Recipe.builder()
                .recipeName("Lasagne")
                .categories(List.of(italiaans, dinner))
                .build();

        recipeRepository.saveAll(List.of(recipe1, recipe2));

        //Act
        List <Recipe> italianRecipes = recipeRepository.findByCategories_CategoryName("Italiaans");

        //Assert
        Assertions.assertNotNull(italianRecipes);
        Assertions.assertEquals(italianRecipes.size(), 2);
    }
}
