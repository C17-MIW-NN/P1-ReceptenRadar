package nl.miwnn.ch17.briljant.receptenradar.service;

import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.RecipeIngredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iris Loermans
 * Handles the making of a new recipe based on an old recipe
 */

@Service
public class RecipeCopyService {

    private final RecipeRepository recipeRepository;

    public RecipeCopyService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe copyFullRecipe(String recipeName) {
        Recipe originalRecipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found : " + recipeName));

        Recipe recipeCopy = copyRecipe(originalRecipe);
        return recipeRepository.save(recipeCopy);
    }

    public Recipe copyRecipe(Recipe original) {
        Recipe copy = new Recipe();

        copySimpleVariables(original, copy);
        copy.setCategories(new ArrayList<>(original.getCategories()));
        copy.setRecipeIngredients(copyIngredients(original, copy));
        copy.setDirections(copyDirections(original, copy));

        return copy;
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
