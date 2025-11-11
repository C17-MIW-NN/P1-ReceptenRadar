package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        datamodel.addAttribute("formIngredient", new Ingredient());

        return ("ingredientForm");
    }

    @PostMapping("/save")
    public String saveOrUpdateRecipe (@ModelAttribute("formIngredient") Ingredient ingredient, BindingResult result) {
        if (!result.hasErrors()) {
            ingredientRepository.save(ingredient);
        }

        return ("redirect:/ingredient/all");
    }
}
