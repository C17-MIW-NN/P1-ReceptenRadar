package nl.miwnn.ch17.briljant.receptenradar.service;

import jakarta.transaction.Transactional;
import nl.miwnn.ch17.briljant.receptenradar.model.*;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Iris Loermans
 * Test to see if a Recipe is copied as it should.
 */

@SpringBootTest
@Transactional
class RecipeCopyServiceTest {

    @Autowired
    private RecipeCopyService recipeCopyService;

    Recipe original = new Recipe();

    @Test
    void testCopyRecipe() {
        original.setRecipeName("Pannenkoeken");
        original.setPreparationTime(10);
        original.setForAmountOfPeople(4);
        original.setCalories(1200);
        original.setImageUrl("IMAGE");
        original.setRecipeDescription("Lekker eten");
        fillListsOfOriginalRecipe();

        Recipe copy = recipeCopyService.copyRecipe(original);

        testSimpleVariables(original, copy);
        testIngredients(original, copy);
        testDirections(original, copy);
    }

    private void fillListsOfOriginalRecipe() {
        Category category = new Category();
        category.setCategoryName("Mijn favoriet");
        original.setCategories(List.of(category));

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("bloem");
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(1000.0);
        recipeIngredient.setUnit("grams");
        original.setRecipeIngredients(List.of(recipeIngredient));

        Direction direction = new Direction();
        direction.setDirectionNumber(1);
        direction.setDirection("Maak de pannenkoeken");
        original.setDirections(List.of(direction));
    }

    private void testSimpleVariables (Recipe original, Recipe copy) {
        assertNull(copy.getRecipeId());
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
}