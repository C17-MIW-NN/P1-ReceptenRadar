package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.*;
import nl.miwnn.ch17.briljant.receptenradar.repositories.*;
import nl.miwnn.ch17.briljant.receptenradar.service.RecipeCopyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.*;

/**
 * @author Iris Loermans
 * Handle requests regarding recipes.
 */

@Controller
public class RecipeController {
    private static final int LIKE = 1;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final ReceptenRadarUserRepository receptenRadarUserRepository;
    private final RecipeCopyService recipeCopyService;

    public RecipeController(RecipeRepository recipeRepository,
                            IngredientRepository ingredientRepository,
                            CategoryRepository categoryRepository,
                            ReceptenRadarUserRepository receptenRadarUserRepository,
                            RecipeCopyService recipeCopyService
    ) {

        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.receptenRadarUserRepository = receptenRadarUserRepository;
        this.recipeCopyService = recipeCopyService;
    }

    @GetMapping({"/recipe/all","/"})
    public String showRecipeOverview(Model datamodel, Principal principal) {
        datamodel.addAttribute("allRecipes", recipeRepository.findAll());
        datamodel.addAttribute("recipe", new Recipe());
        datamodel.addAttribute("allCategories",
                categoryRepository.findAll());

        if (principal != null) {
            Optional<ReceptenRadarUser> optionalUser =
                    receptenRadarUserRepository.findByUsername(principal.getName());

            if (optionalUser.isPresent()) {
                ReceptenRadarUser user = optionalUser.get();
                datamodel.addAttribute("user", user);

                Map<Category,Integer> userLikesPerCategory = new HashMap<>();
                for (Recipe recipe : user.getLikedRecipes()) {
                    for (Category category : recipe.getCategories()) {
                        int currentCount = userLikesPerCategory.getOrDefault(category, 0);
                        userLikesPerCategory.put(category, currentCount + LIKE);
                    }
                }

                List<Category> sortedCategories = new ArrayList<>(userLikesPerCategory.keySet());
                sortedCategories.sort(Comparator.comparingInt(userLikesPerCategory::get).reversed());

                datamodel.addAttribute("userFavoriteCategories", sortedCategories);
            }
        } else {
            datamodel.addAttribute("user", null);

        }

        return "recipeOverview";
    }


    @GetMapping("/recipe/all/{categoryName}")
    public String showRecipesPerCategory(
            @PathVariable String categoryName,
            Model datamodel,
            Principal principal) {

        List<Recipe> recipes = recipeRepository.findByCategories_CategoryName(categoryName);

        datamodel.addAttribute("allRecipes", recipes);
        datamodel.addAttribute("selectedCategory", categoryName);
        datamodel.addAttribute("allCategories", categoryRepository.findAllByOrderByCategoryLikesDesc());

        if (principal != null) {
            receptenRadarUserRepository.findByUsername(principal.getName())
                    .ifPresent(user -> datamodel.addAttribute("user", user));
        }

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
    public String saveOrUpdateRecipe (@ModelAttribute("formRecipe") Recipe recipeToBeSaved, BindingResult result,
                                      Model datamodel) {

        Optional<Recipe> recipeWithSameName = recipeRepository.findByRecipeName(recipeToBeSaved.getRecipeName());
        if (recipeWithSameName.isPresent() &&
                !recipeWithSameName.get().getRecipeId().equals(recipeToBeSaved.getRecipeId())) {
            result.addError(new FieldError("recipe", "recipeName",
                    "This name is already in use by another recipe"));
        }

        if (!result.hasErrors()) {
            if (recipeToBeSaved.getDirections() != null) {
                recipeToBeSaved.getDirections().removeIf(d -> d.getDirection() == null ||
                        d.getDirection().isBlank());
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

    @PostMapping("/recipe/{recipeId}/like")
    public String likeRecipeAndCategories(@PathVariable Long recipeId, Model datamodel, Principal principal) {
        ReceptenRadarUser user = receptenRadarUserRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found for user:" + principal.getName()));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!user.getLikedRecipes().contains(recipe)) {
            user.getLikedRecipes().add(recipe);
            recipe.getRecipeLikes().add(user);

            for (Category category : recipe.getCategories()) {
                category.setCategoryLikes(category.getCategoryLikes() + LIKE);
            }

            recipeRepository.save(recipe);
            receptenRadarUserRepository.save(user);
            categoryRepository.saveAll(recipe.getCategories());
        }

        datamodel.addAttribute("recipe", recipe);
        datamodel.addAttribute("user", user);
        datamodel.addAttribute("allCategories", categoryRepository.findAll());
        datamodel.addAttribute("numberOfLikes", recipe.getRecipeLikes().size());

        return "recipeDetail";
    }

    @GetMapping("/recipe/detail/{recipeName}/copy")
    public String copyRecipe(@PathVariable String recipeName) {
        Recipe copy = recipeCopyService.copyFullRecipe(recipeName);
        return "redirect:/recipe/detail/" + copy.getRecipeName();
    }
}
