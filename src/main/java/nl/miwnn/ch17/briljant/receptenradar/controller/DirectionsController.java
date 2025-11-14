package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Direction;
import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import nl.miwnn.ch17.briljant.receptenradar.repositories.DirectionsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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
        datamodel.addAttribute("direction", new Direction());

        return "DirectionsForm";
    }

    public String showDirectionsForm (Model datamodel, Direction direction) {
        datamodel.addAttribute("DirectionForm", direction);
        datamodel.addAttribute("allDirections", directionsRepository.findAll());

        return "DirectionForm";
    }

    @PostMapping("/add-step")
    public String addStep(@ModelAttribute Direction direction, Model datamodel) {
        // Add a new empty step to the list
        direction.getSteps().add("");
        datamodel.addAttribute("direction", direction);
        return "DirectionsForm";
    }

    @PostMapping("/save")
    public String saveOrUpdateRecipe(@ModelAttribute ("DirectionsForm") Direction directionToBeSaved,
                                     BindingResult result, Model datamodel) {

            directionsRepository.save(directionToBeSaved);

            if (!result.hasErrors()) {
                directionsRepository.save(directionToBeSaved);
            }

        return "redirect:/recipe/detail/" + directionToBeSaved;
    }
}
