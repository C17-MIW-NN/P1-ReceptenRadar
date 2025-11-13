package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Directions;
import nl.miwnn.ch17.briljant.receptenradar.model.MockRecipe;
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
        MockRecipe recipe = new MockRecipe();
        recipe.getSteps().add("Preheat oven to 180°C");
        recipe.getSteps().add("Mix ingredients");
//        recipe.getSteps().add(new Directions("Preheat oven to 180°C"));
//        recipe.getSteps().add(new Directions("Mix ingredients"));
        datamodel.addAttribute("recipe", recipe);

        return "DirectionsForm";
    }

    @PostMapping("/add-step")
    public String addStep(@ModelAttribute MockRecipe recipe, Model model) {
        // Add a new empty step to the list
        recipe.getSteps().add("");
//        recipe.getSteps().add(new Directions());
        model.addAttribute("recipe", recipe);
        return "DirectionsForm"; // re-render the same form with the new field
    }

    @PostMapping("/save")
    public String saveRecipe(@ModelAttribute MockRecipe recipe) {
        // TODO: persist or process recipe
        System.out.println("Recept opgeslagen: " + recipe.getName());
        recipe.getSteps().forEach(step -> System.out.println(" - " + step));
//        recipe.getSteps().forEach(step -> System.out.println(" - " + step.getDescription()));
        return "redirect:/recipeOverview";
    }

//    @GetMapping("/directions")
//    public String showDirectionsOverview(Model datamodel) {
//
//        List<Directions> allDirectionsMethods = new ArrayList<>();
//        List<String> steps = new ArrayList<>();
//
//        steps.add("""
//                Snipper de ui en snijd de knoflook fijn. Verhit ⅔ van de olie in een soeppan\s
//                en fruit de ui, knoflook, paprikapoeder, ⅔ van de chipotle 3 min. Voeg de bevroren pompoenstukjes toe en\s
//                bak 5 min. Voeg het water toe en verkruimel het bouillonblokje erboven. Breng de soep aan de kook, zet\s
//                het vuur laag en kook in 20 min. gaar.""");
//        steps.add("""
//                Verhit ondertussen de rest van de olie in een koekenpan en bak de tempeh 5 min. op middelhoog vuur, roer\s
//                regelmatig. Snijd het bosuitje in dunne ringen en de koriander grof. Pers de limoen uit""");
//        steps.add("""
//                Bak de naan volgens de aanwijzingen op de verpakking. Voeg ⅘ van de kokosmelk toe aan de soep en pureer\s
//                met de staafmixer. Breng op smaak met het limoensap, peper en eventueel zout.""");
//        steps.add("""
//                Verdeel over kommen en verdeel de tempeh, bosui, koriander en gebakken uitjes erover. Besprenkel met de\s
//                rest van de kokosmelk en de rest van de chipotle. Snijd de naan in stukken en serveer bij de soep.""");
//
//        datamodel.addAttribute("steps", steps);
//
//        allDirectionsMethods.add(new Directions(steps));
//
//        return "DirectionsForm";
//    }



}
