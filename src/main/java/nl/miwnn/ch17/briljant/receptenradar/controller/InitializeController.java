package nl.miwnn.ch17.briljant.receptenradar.controller;

import com.opencsv.CSVReader;
import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.RecipeIngredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Johan Elzinga
 * Initialises the database with example data
 */

@Controller
public class InitializeController {
    private static final String SAMPLEDATA_INGREDIENTS_CSV = "/sampledata/ingredients.csv";
    private static final String SAMPLEDATA_RECIPES_CSV = "/sampledata/recipes.csv";
    private static final double DEFAULT_QUANTITY = 1.0;
    private static final String DEFAULT_UNIT = "unknown";

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final Map<String, Ingredient> ingredientCache;

    public InitializeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        ingredientCache = new HashMap<>();
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDatabase();
        }
    }

    private void initializeDatabase() {
        loadIngredients();
        loadRecipes();
    }

    private void loadIngredients() {
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(SAMPLEDATA_INGREDIENTS_CSV)
                .getFile()))) {

            reader.skip(1);

            for (String[] ingredientLine : reader) {
                Ingredient ingredient =  new Ingredient();
                ingredient.setIngredientName(ingredientLine[0]);
                ingredientRepository.save(ingredient);
                ingredientCache.put(ingredientLine[0], ingredient);
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private void loadRecipes() {
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(SAMPLEDATA_RECIPES_CSV)
                .getFile()))) {

            reader.skip(1);

            for (String[] recipeLine : reader) {

                Recipe recipe = makeRecipe(
                        recipeLine[0],
                        Integer.parseInt(recipeLine[1]),
                        Integer.parseInt(recipeLine[2]),
                        Integer.parseInt(recipeLine[3]),
                        recipeLine[4]);

                addRecipeIngredient(recipeLine[5], recipe);
                recipeRepository.save(recipe);
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private Recipe makeRecipe(String recipeName, int preparationTime, int forAmountOfPeople, int calories,
                             String coverImageUrl) {
        Recipe recipe = new Recipe();

        recipe.setRecipeName(recipeName);
        recipe.setPreparationTime(preparationTime);
        recipe.setForAmountOfPeople(forAmountOfPeople);
        recipe.setCalories(calories);
        recipe.setImageUrl(coverImageUrl);
        recipe.setRecipeIngredients(new HashSet<>());

        return recipe;
    }

    private void addRecipeIngredient(String recipeLine, Recipe recipe) {

        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for (String ingredientName : recipeLine.split(", ")) {
            Ingredient ingredient = ingredientCache.get(ingredientName);

            if (ingredient == null) {
                System.out.println("Ingredient niet gevonden: " + ingredientName);
                continue;
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(DEFAULT_QUANTITY);
            recipeIngredient.setUnit(DEFAULT_UNIT);

            recipeIngredients.add(recipeIngredient);

        }
        recipe.setRecipeIngredients(recipeIngredients);

    }
}
