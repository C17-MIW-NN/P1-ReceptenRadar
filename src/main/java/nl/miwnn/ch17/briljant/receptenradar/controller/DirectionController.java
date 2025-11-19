package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iris Loermans
 * Handles requests regarding the directions from a specific recipe.
 */

@Controller
@RequestMapping("/recipe/detail/{recipeName}")
public class DirectionController {
    private final RecipeRepository recipeRepository;

    public DirectionController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/directions")
    public String showDirectionsForm(@PathVariable("recipeName") String recipeName, Model datamodel) {
        Recipe recipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new RuntimeException("Recept niet gevonden"));

        if (recipe.getDirections() == null) {
            recipe.setDirections(new ArrayList<>());

            if (recipe.getDirections().isEmpty() ||
                    recipe.getDirections().get(recipe.getDirections().size() - 1).getDirection() != null &&
                            !recipe.getDirections().get(recipe.getDirections().size() - 1).getDirection().isEmpty()) {

                Direction empty = new Direction();
                empty.setDirection("");
                recipe.getDirections().add(empty);
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
                .orElseThrow(() -> new RuntimeException("Recept niet gevonden"));

        recipe.getDirections().clear();

        List<Direction> directions = recipeToSave.getDirections();

        int stepNumber = 1;
        for (Direction index : directions) {
            if (index.getDirection() == null || index.getDirection().trim().isEmpty()) {
                continue;
            }

            index.setDirectionNumber(stepNumber++);
            index.setRecipe(recipe);

            recipe.getDirections().add(index);
        }

        recipeRepository.save(recipe);

        return "redirect:/recipe/detail/" + recipeName;
    }
}

