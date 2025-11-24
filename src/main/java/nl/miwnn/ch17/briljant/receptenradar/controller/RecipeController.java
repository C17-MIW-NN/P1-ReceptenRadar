package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import nl.miwnn.ch17.briljant.receptenradar.repositories.RecipeRepository;
import nl.miwnn.ch17.briljant.receptenradar.service.RecipeCopyService;
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

/**
 * @author Iris Loermans
 * Handle requests regarding recipes.
 */

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeCopyService recipeCopyService;


    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, CategoryRepository categoryRepository, RecipeCopyService recipeCopyService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.recipeCopyService = recipeCopyService;
    }

    @GetMapping({"/recipe/all","/"})
    public String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepository.findAll());

        datamodel.addAttribute("allRecipes", recipeRepository.findAll());
        datamodel.addAttribute("recipe", new Recipe());
        datamodel.addAttribute("allCategories", categoryRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        Recipe newRecipe = new Recipe();
        newRecipe.getDirections().add(new Direction());
        datamodel.addAttribute("recipe", newRecipe);
        datamodel.addAttribute("allCategories", categoryRepository.findAll());

        return "recipeForm";
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

        if (!result.hasErrors()) {
            if (recipeToBeSaved.getDirections() != null) {
                recipeToBeSaved.getDirections().removeIf(d -> d.getDirection() == null || d.getDirection().isBlank());
                for (Direction direction : recipeToBeSaved.getDirections()) {
                    direction.setRecipe(recipeToBeSaved);
                }
            }

            recipeRepository.save(recipeToBeSaved);
        }

        return "redirect:/recipe/detail/" + recipeToBeSaved.getRecipeName();
    }

    @PostMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        System.out.println("Deleting recipe with id: " + recipeId);
        recipeRepository.deleteById(recipeId);
        return "redirect:/recipe/all";
    }



    @GetMapping("recipe/detail/{recipeId}")
    public String showRecipeDetailPage(@PathVariable("recipeId") String recipeName, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByRecipeName(recipeName);

        if (recipeToShow.isEmpty()){
            return "redirect:/recipe/all";
        }

        datamodel.addAttribute("recipe", recipeToShow.get());
        datamodel.addAttribute("allCategories", categoryRepository.findAll());
        datamodel.addAttribute("allIngredients",ingredientRepository.findAll());
        return "recipeDetail";
    }

    @GetMapping("/recipe/detail/{recipeName}/copy")
    public String copyRecipe(@PathVariable String recipeName) {
        Recipe copy = recipeCopyService.copyFullRecipe(recipeName);

        return "redirect:/recipe/detail/" + copy.getRecipeName();
    }

}
