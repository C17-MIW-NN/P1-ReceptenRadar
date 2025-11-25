package nl.miwnn.ch17.briljant.receptenradar.service;

import jakarta.transaction.Transactional;
import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.RecipeIngredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Iris Loermans
 * purpose of the code
 */

@SpringBootTest
@Transactional
class RecipeCopyServiceTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCopyService recipeCopyService;

    @Test
    void copyRecipe() {
        Recipe original = recipeRepository.findByRecipeName("Pompoensoep met zoet-pittige tempeh")
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found : "
                        + "Pompoensoep met zoet-pittige tempeh"));
        Recipe copy = new Recipe();

        copySimpleVariables(original, copy);
        copy.setCategories(new ArrayList<>(original.getCategories()));
        copy.setRecipeIngredients(copyIngredients(original, copy));
        copy.setDirections(copyDirections(original, copy));

        testSimpleVariables(original, copy);
        testIngredients(original, copy);
        testDirections(original, copy);
    }

    private void testSimpleVariables (Recipe original, Recipe copy) {
        assertNotEquals(original.getRecipeId(), copy.getRecipeId());
        assertNotEquals(original.getRecipeName(), copy.getRecipeName());

        assertEquals(original.getPreparationTime(), copy.getPreparationTime());
        assertEquals(original.getForAmountOfPeople(), copy.getForAmountOfPeople());
        assertEquals(original.getCalories(), copy.getCalories());
        assertEquals(original.getImageUrl(), copy.getImageUrl());
        assertEquals(original.getRecipeDescription(), copy.getRecipeDescription());

        assertIterableEquals(original.getCategories(), copy.getCategories());
        assertNotSame(original.getCategories(), copy.getCategories());
    }

    private void testIngredients (Recipe original, Recipe copy) {
        assertEquals(original.getRecipeIngredients().size(), copy.getRecipeIngredients().size());
        for (int index = 0; index < original.getRecipeIngredients().size(); index++) {
            assertEquals(original.getRecipeIngredients().get(index).getIngredient(),
                    copy.getRecipeIngredients().get(index).getIngredient());
            assertEquals(original.getRecipeIngredients().get(index).getQuantity(),
                    copy.getRecipeIngredients().get(index).getQuantity());
            assertEquals(original.getRecipeIngredients().get(index).getUnit(),
                    copy.getRecipeIngredients().get(index).getUnit());

            assertNotSame(original.getRecipeIngredients().get(index),  copy.getRecipeIngredients().get(index));
        }
    }

    private void testDirections (Recipe original, Recipe copy) {
        assertEquals(original.getDirections().size(), copy.getDirections().size());
        for (int index = 0; index < original.getDirections().size(); index++) {
            assertEquals(original.getDirections().get(index).getDirectionNumber(),
                    copy.getDirections().get(index).getDirectionNumber());
            assertEquals(original.getDirections().get(index).getDirection(),
                    copy.getDirections().get(index).getDirection());

            assertNotSame(original.getDirections().get(index), copy.getDirections().get(index));
        }
    }

    private void copySimpleVariables(Recipe original, Recipe recipeCopy) {
        recipeCopy.setRecipeName(generateNewCopyName(original.getRecipeName()));
        recipeCopy.setPreparationTime(original.getPreparationTime());
        recipeCopy.setForAmountOfPeople(original.getForAmountOfPeople());
        recipeCopy.setCalories(original.getCalories());
        recipeCopy.setImageUrl(original.getImageUrl());
        recipeCopy.setRecipeDescription(original.getRecipeDescription());
    }

    private String generateNewCopyName(String baseName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "UnknownUser";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + auth);
        System.out.println("Username: " + (auth != null ? auth.getName() : "null"));

        return baseName + " a la " + username;
    }

    private List<RecipeIngredient> copyIngredients(Recipe original, Recipe recipeCopy) {
        List<RecipeIngredient> ingredients = new ArrayList<>();

        for (RecipeIngredient originalIngredient : original.getRecipeIngredients()) {
            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setQuantity(originalIngredient.getQuantity());
            ingredient.setUnit(originalIngredient.getUnit());
            ingredient.setIngredient(originalIngredient.getIngredient());
            ingredient.setRecipe(recipeCopy);

            ingredients.add(ingredient);
        }

        return ingredients;
    }

    private List<Direction> copyDirections(Recipe original, Recipe recipeCopy) {
        List<Direction> directions = new ArrayList<>();

        for (Direction originalDirection : original.getDirections()) {
            Direction direction = new Direction();
            direction.setDirectionNumber(originalDirection.getDirectionNumber());
            direction.setDirection(originalDirection.getDirection());
            direction.setRecipe(recipeCopy);

            directions.add(direction);
        }

        return directions;
    }
}