package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Iris Loermans
 * Handles requests regarding the directions from a specific recipe.
 */

@Controller
@RequestMapping("/recipe/detail/{recipeName}")
public class DirectionsController {
    private final RecipeRepository recipeRepository;

    public DirectionsController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/directions")
    public String showDirectionsForm(@PathVariable("recipeName") String recipeName, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByRecipeName(recipeName);

        if (recipeToShow.isPresent()) {
            Recipe recipe = recipeToShow.get();

            if (recipe.getSteps() == null) {
                recipe.setSteps(new ArrayList<>());
            }

            if (recipe.getDirections() != null && !recipe.getDirections().isEmpty()) {
                recipe.setSteps(new ArrayList<>(Arrays.asList(recipe.getDirections().split(";"))));
            }

            if (recipe.getSteps().isEmpty() || !recipe.getSteps().get(recipe.getSteps().size() - 1).isEmpty()) {
                recipe.getSteps().add("");
            }

            datamodel.addAttribute("recipe", recipe);
            return "DirectionsForm";
        }
        return "redirect:/recipe/detail/{recipeName}";
    }

    @PostMapping("/directions/save")
    public String saveOrUpdateRecipe(
            @PathVariable("recipeName") String recipeName,
            @ModelAttribute Recipe recipeToSave) {

        Recipe recipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        String directions = String.join(";", recipeToSave.getSteps());
        recipe.setDirections(directions);

        recipeRepository.save(recipe);

        return "redirect:/recipe/detail/" + recipeName;
    }
}

