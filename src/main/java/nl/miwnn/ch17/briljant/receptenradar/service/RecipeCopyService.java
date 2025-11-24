package nl.miwnn.ch17.briljant.receptenradar.service;

import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.RecipeIngredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iris Loermans
 * Handles the making of a new recipe based on an old recipe
 */

@Service
public class RecipeCopyService {

    private RecipeRepository recipeRepository;

    public RecipeCopyService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe copyFullRecipe(String recipeName) {
        Recipe originalRecipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found : " + recipeName));

        Recipe recipeCopy = copyRecipe(originalRecipe);
        return recipeRepository.save(recipeCopy);
    }

    public Recipe copyRecipe(Recipe originalRecipe) {
        Recipe recipeCopy = new Recipe();

        recipeCopy.setRecipeName(generateNewCopyName(originalRecipe.getRecipeName()));

        recipeCopy.setPreparationTime(originalRecipe.getPreparationTime());
        recipeCopy.setForAmountOfPeople(originalRecipe.getForAmountOfPeople());
        recipeCopy.setCalories(originalRecipe.getCalories());
        recipeCopy.setImageUrl(originalRecipe.getImageUrl());
        recipeCopy.setRecipeDescription(originalRecipe.getRecipeDescription());

        recipeCopy.setCategories(new ArrayList<>(originalRecipe.getCategories()));

        List<RecipeIngredient> ingredientCopies = new ArrayList<>();
        for (RecipeIngredient ingredient : originalRecipe.getRecipeIngredients()) {
            RecipeIngredient newIngredient = new RecipeIngredient();
            newIngredient.setQuantity(ingredient.getQuantity());
            newIngredient.setUnit(ingredient.getUnit());
            newIngredient.setIngredient(ingredient.getIngredient());
            newIngredient.setRecipe(recipeCopy);

            ingredientCopies.add(newIngredient);
        }
        recipeCopy.setRecipeIngredients(ingredientCopies);

        List<Direction> directionCopies = new ArrayList<>();
        for (Direction direction : originalRecipe.getDirections()) {
            Direction newDirection = new Direction();
            newDirection.setDirectionNumber(direction.getDirectionNumber());
            newDirection.setDirection(direction.getDirection());
            newDirection.setRecipe(recipeCopy);
            directionCopies.add(newDirection);
        }
        recipeCopy.setDirections(directionCopies);

        return recipeCopy;
    }

    private String generateNewCopyName(String baseName) {
        return baseName + " a la Piet";
    }
}
