package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Iris Loermans
 * Handle requests regarding recipes.
 */

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;


    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping({"/recipe/all","/"})
    public String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepository.findAll());

        datamodel.addAttribute("allRecipes", recipeRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        datamodel.addAttribute("formRecipe", new Recipe());

        return ("recipeForm");
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe (@ModelAttribute("formRecipe") Recipe recipe, BindingResult result) {
        if (!result.hasErrors()) {
            recipeRepository.save(recipe);
        }

        return ("redirect:/recipe/all");
    }

    @GetMapping("recipe/detail/{recipeName}")
    public String showRecipeDetailPage(@PathVariable("recipeName") String recipeName, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByRecipeName(recipeName);

        if (recipeToShow.isEmpty()){
            return "redirect:/recipe/all";
        }

        datamodel.addAttribute("recipe", recipeToShow.get());
        return "recipeDetail";
    }

}
