package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
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
        Direction direction = new Direction();
        datamodel.addAttribute("direction", direction);

        return "DirectionsForm";
    }

    @PostMapping("/add-step")
    public String addStep(@ModelAttribute Direction direction, Model datamodel) {
        // Add a new empty step to the list
        direction.getSteps().add("");
        datamodel.addAttribute("direction", direction);
        return "DirectionsForm";
    }

    @PostMapping("/save")
    public String saveRecipe(@ModelAttribute Direction direction) {
        direction.getSteps().forEach(step -> System.out.println(" - " + step));
        return "redirect:/recipeOverview";
    }
}
