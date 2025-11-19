package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Iris Loermans
 * Handle requests regarding recipes.
 */

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;


    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"/recipe/all","/"})
    public String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepository.findAll());

        datamodel.addAttribute("allRecipes", recipeRepository.findAll());
        datamodel.addAttribute("formRecipe", new Recipe());
        datamodel.addAttribute("allCategories", categoryRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        datamodel.addAttribute("formRecipe", new Recipe());

        return ("recipeForm");
    }

    public String showRecipeForm (Model datamodel, Recipe recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("allRecipes", recipeRepository.findAll());
        datamodel.addAttribute("allIngredients",ingredientRepository.findAll());
        datamodel.addAttribute("allCategories",categoryRepository.findAll());

        return "recipeForm";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe (@ModelAttribute("formRecipe") Recipe recipeToBeSaved, BindingResult result, Model datamodel) {

        Optional<Recipe> recipeWithSameName = recipeRepository.findByRecipeName(recipeToBeSaved.getRecipeName());
        if (recipeWithSameName.isPresent() && !recipeWithSameName.get().getRecipeId().equals(recipeToBeSaved.getRecipeId())) {
            result.addError(new FieldError("recipe", "recipeName",
                    "This name is already in use by another recipe"));
        }
        boolean isNewRecipe = (recipeToBeSaved.getRecipeId() == null);

        if (!result.hasErrors()) {
            if (recipeToBeSaved.getDirections() != null) {
                recipeToBeSaved.getDirections().removeIf(d -> d.getDirection() == null || d.getDirection().isBlank());
                for (Direction direction : recipeToBeSaved.getDirections()) {
                    direction.setRecipe(recipeToBeSaved);
                }
            }

            recipeRepository.save(recipeToBeSaved);
        }


        return isNewRecipe
                ? "redirect:/recipe/edit/" + recipeToBeSaved.getRecipeName()
                : "redirect:/recipe/detail/" + recipeToBeSaved.getRecipeName();
    }

    @GetMapping("recipe/detail/{recipeName}")
    public String showRecipeDetailPage(@PathVariable("recipeName") String recipeName, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByRecipeName(recipeName);

        if (recipeToShow.isEmpty()){
            return "redirect:/recipe/all";
        }

        datamodel.addAttribute("recipe", recipeToShow.get());
        datamodel.addAttribute("allCategories", categoryRepository.findAll());
        datamodel.addAttribute("allIngredients",ingredientRepository.findAll());
        return "recipeDetail";
    }

    @GetMapping("/recipe/edit/{recipeName}")
    public String showRecipeEditPage(@PathVariable("recipeName") String recipeName, Model datamodel) {
        Optional <Recipe> recipeToEdit = recipeRepository.findByRecipeName(recipeName);

        if (recipeToEdit.isEmpty()) {
            return "redirect:/recipe/all";
        }

        List<Category> allCategories = categoryRepository.findAll();
        datamodel.addAttribute("allCategories", allCategories);
        datamodel.addAttribute("allIngredients",ingredientRepository.findAll());
        datamodel.addAttribute("recipe", recipeToEdit.get());

        return "recipeEdit";
    }

}
