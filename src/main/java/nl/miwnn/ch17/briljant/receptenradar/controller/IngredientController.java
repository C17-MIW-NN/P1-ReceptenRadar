package nl.miwnn.ch17.briljant.receptenradar.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Iris Loermans
 * Handle requests regarding ingredients.
 */

@Controller
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/all")
    public String showRecipeOverview(Model datamodel) {
        datamodel.addAttribute("allIngredients", ingredientRepository.findAll());

        return "ingredientOverview";
    }

    @GetMapping("/add")
    public String showRecipeForm(Model datamodel) {
        return showIngredientForm(datamodel, new Ingredient());
    }

    @GetMapping("/edit/{ingredientName}")
    public String showEditIngredientForm(@PathVariable("ingredientName") String ingredientName, Model datamodel) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByIngredientName(ingredientName);

        if (optionalIngredient.isPresent()) {
            return showIngredientForm(datamodel, optionalIngredient.get());
        }

        return ("redirect:/ingredient/all");
    }

    private String showIngredientForm(Model datamodel, Ingredient ingredient) {
        datamodel.addAttribute("formIngredient", ingredient);

        return "ingredientForm";
    }

    @PostMapping("/save")
    public String saveOrUpdateRecipe (@ModelAttribute("formIngredient") Ingredient ingredient, BindingResult result) {
        if (!result.hasErrors()) {
            ingredientRepository.save(ingredient);
        }

        return ("redirect:/ingredient/all");
    }
}
