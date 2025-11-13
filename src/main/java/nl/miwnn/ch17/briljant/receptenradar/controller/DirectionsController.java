package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.DirectionsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Iris Loermans
 * Handles requests regarding Directions.
 */

@Controller
@RequestMapping("/directions")
public class DirectionsController {
    private DirectionsRepository directionsRepository;

    public DirectionsController(DirectionsRepository directionsRepository) {
        this.directionsRepository = directionsRepository;
    }

    @GetMapping
    public String showDirectionsForm(Model datamodel) {
        Recipe recipe = new Recipe();
        datamodel.addAttribute("recipe", recipe);

        return "DirectionsForm";
    }

    @PostMapping("/add-step")
    public String addStep(@ModelAttribute Recipe recipe, Model model) {
        // Add a new empty step to the list
        recipe.getSteps().add("");
        model.addAttribute("recipe", recipe);
        return "DirectionsForm";
    }

    @PostMapping("/save")
    public String saveRecipe(@ModelAttribute Recipe recipe) {
        // TODO: persist or process recipe
        System.out.println("Recept opgeslagen: " + recipe.getRecipeName());
        recipe.getSteps().forEach(step -> System.out.println(" - " + step));
        return "redirect:/recipeOverview";
    }
}
