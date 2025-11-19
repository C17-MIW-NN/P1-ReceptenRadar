package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.repositories.DirectionRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author Iris Loermans
 * Handles requests regarding the directions from a specific recipe.
 */

@Controller
@RequestMapping("/recipe/detail/{recipeName}")
public class DirectionController {

    private final RecipeRepository recipeRepository;
    private final DirectionRepository directionRepository;

    public DirectionController(RecipeRepository recipeRepository, DirectionRepository directionRepository) {
        this.recipeRepository = recipeRepository;
        this.directionRepository = directionRepository;
    }

    @GetMapping("/directions")
    public String showDirectionsForm(@PathVariable("recipeName") String recipeName, Model model) {
        Recipe recipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new RuntimeException("Recept niet gevonden"));

        if (recipe.getDirections() == null) {
            recipe.setDirections(new ArrayList<>());
        }

        if (recipe.getDirections().isEmpty() ||
                recipe.getDirections().get(recipe.getDirections().size() - 1).getDirection() != null &&
                        !recipe.getDirections().get(recipe.getDirections().size() - 1).getDirection().isEmpty()) {

            Direction empty = new Direction();
            empty.setDirection("");
            empty.setRecipe(recipe);
            recipe.getDirections().add(empty);
        }

        model.addAttribute("recipe", recipe);
        return "DirectionsForm";
    }

    @PostMapping("/directions/save")
    public String saveDirections(@PathVariable("recipeName") String recipeName,
                                 @ModelAttribute Recipe recipeForm) {

        Recipe recipe = recipeRepository.findByRecipeName(recipeName)
                .orElseThrow(() -> new RuntimeException("Recept niet gevonden"));

        recipe.getDirections().clear();

        if (recipeForm.getDirections() != null) {
            for (Direction direction : recipeForm.getDirections()) {
                if (direction.getDirection() != null && !direction.getDirection().isEmpty()) {
                    direction.setRecipe(recipe);
                    recipe.getDirections().add(direction);
                }
            }
        }

        recipeRepository.save(recipe);
        return "redirect:/recipe/detail/" + recipeName;
    }
}
