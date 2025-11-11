package nl.miwnn.ch17.briljant.receptenradar.controller;

import com.opencsv.CSVReader;
import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
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
public class InitializerController {
    private static final String SAMPLEDATA_INGREDIENTS_CSV = "/sampledata/ingredients.csv";
    private static final String SAMPLEDATA_RECIPES_CSV = "/sampledata/recipes.csv";

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final Map<String, Ingredient> ingredientCache;

    public InitializerController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
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
            // First line contains the header so we skip this line
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
            // First line contains the header so we skip this line
            reader.skip(1);

            for (String[] recipeLine : reader) {
                Recipe recipe =  new Recipe();
                recipe.setRecipeName(recipeLine[0]);
                recipe.setPreparationTime(Integer.parseInt(recipeLine[1]));
                recipe.setForAmountOfPeople(Integer.parseInt(recipeLine[2]));
                recipe.setCalories(Integer.parseInt(recipeLine[3]));
                recipe.setCoverImageUrl(recipeLine[4]);
                recipe.setIngredients(new HashSet<>());

                for (String ingredientName : recipeLine[5].split(", ")) {
                    recipe.getIngredients().add(ingredientCache.get(ingredientName));
                }
                recipeRepository.save(recipe);
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}
