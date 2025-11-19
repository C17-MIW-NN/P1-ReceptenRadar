package nl.miwnn.ch17.briljant.receptenradar.controller;

import com.opencsv.CSVReader;
import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.RecipeIngredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
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
    private static final String SAMPLE_CATEGORIES_CSV = "/sampledata/categories.csv";
    private static final double DEFAULT_QUANTITY = 1.0;
    private static final String DEFAULT_UNIT = "unknown";

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final Map<String, Ingredient> ingredientCache;
    private final Map<String, Category> categoryCache;

    public InitializeController(RecipeRepository recipeRepository,
                                IngredientRepository ingredientRepository,
                                CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        ingredientCache = new HashMap<>();
        categoryCache = new HashMap<>();
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDatabase();
        }
    }

    private void initializeDatabase() {
        loadIngredients();
        loadCategories();
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

    private void loadCategories() {
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(SAMPLE_CATEGORIES_CSV)
                .getFile()))) {

            reader.skip(1);

            for (String[] categoryLine : reader) {
                Category category =  new Category();
                category.setCategoryName(categoryLine[0]);
                categoryRepository.save(category);
                categoryCache.put(categoryLine[0], category);
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

                Recipe recipe = makeRecipe(recipeLine);

                addRecipeIngredient(recipeLine[5], recipe);

                addCategory(recipeLine[6], recipe);

                recipeRepository.save(recipe);
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private Recipe makeRecipe(String[] recipeLine) {
        Recipe recipe = new Recipe();

        recipe.setRecipeName(recipeLine[0]);
        recipe.setPreparationTime(Integer.parseInt(recipeLine[1]));
        recipe.setForAmountOfPeople(Integer.parseInt(recipeLine[2]));
        recipe.setCalories(Integer.parseInt(recipeLine[3]));
        recipe.setImageUrl(recipeLine[4]);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setRecipeDescription(recipeLine[7]);

        return recipe;
    }

    private void addRecipeIngredient(String recipeLine, Recipe recipe) {

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

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

    private void addCategory(String recipeLine, Recipe recipe) {
        List<Category> categories = new ArrayList<>();

        for (String categoryName : recipeLine.split(", ")) {
            Category category = categoryCache.get(categoryName);

            if (category == null) {
                System.out.println("Categorie niet gevonden: " + categoryName);
                continue;
            }

            categories.add(category);
        }

        recipe.setCategories(categories);
    }
}
