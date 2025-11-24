package nl.miwnn.ch17.briljant.receptenradar;

import nl.miwnn.ch17.briljant.receptenradar.controller.InitializeController;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Johan Elzinga
 */

//public class InitializeControllerTest {
//
//    private RecipeRepository recipeRepository;
//    private IngredientRepository ingredientRepository;
//    private CategoryRepository categoryRepository;
//
//
//    @Test
//    @DisplayName("Testing the makeRecipe method in the InitializeControle")
//    void makeRecipeTest () {
//        // Arrange
//        String[] testDataString = {"Pompoensoep met zoet-pittige tempeh","30","4","665",
//                "https://static.ah.nl/static/recepten/img_RAM_PRD211895_1224x900_JPG.jpg",
//                "ui, knoflook, zonnebloemolie, pompoenstukjes, water","Hollands, Lunch",
//                "Dit is een onzinnige beschrijving van dit recept :-)"};
//        int expectedPreparationTime = 30;
//
//        // Act
//        Recipe recipeTestObject = new InitializeController(recipeRepository,
//                ingredientRepository, categoryRepository).makeRecipe(testDataString);
//
//        // Assert
//        assertEquals(expectedPreparationTime, recipeTestObject.getPreparationTime());
//
//    }
//}
