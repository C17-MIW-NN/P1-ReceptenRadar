package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.model.Ingredient;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
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
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/all")
    public String showCategoryOverview(Model datamodel) {
        datamodel.addAttribute("allCategories", categoryRepository.findAll());

        return "categoryOverview";
    }

    @GetMapping("/add")
    public String showCategoryForm(Model datamodel) {
        datamodel.addAttribute("formCategory", new Category());

        return ("categoryForm");
    }

    @PostMapping("/save")
    public String saveOrUpdateCategpry (@ModelAttribute("formCatagory") Category category, BindingResult result) {
        if (!result.hasErrors()) {
            categoryRepository.save(category);
        }

        return ("redirect:/category/all");
    }
}
