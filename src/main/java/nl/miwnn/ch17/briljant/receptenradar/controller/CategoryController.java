package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import nl.miwnn.ch17.briljant.receptenradar.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Johan Elzinga
 * Handle requests regarding categories.
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
        return showCategoryForm(datamodel, new Category());
    }

    @GetMapping("/edit/{categoryName}")
    public String showEditCategoryForm(@PathVariable("categoryName") String categoryName, Model datamodel) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);

        if (optionalCategory.isPresent()) {
            return showCategoryForm(datamodel, optionalCategory.get());
        }

        return ("redirect:/category/all");
    }

    private String showCategoryForm(Model datamodel, Category category) {
        datamodel.addAttribute("formCategory", category);

        return "categoryForm";
    }

    @PostMapping("/save")
    public String saveOrUpdateCategpry (@ModelAttribute("formCatagory") Category categoryToBeSaved,
                                        BindingResult result,
                                        Model datamodel) {
        Optional<Category> categoryWithSameName = categoryRepository
                                                    .findByCategoryName(categoryToBeSaved.getCategoryName());
        if (categoryWithSameName.isPresent() &&
                !categoryWithSameName.get().getCategoryId().equals(categoryToBeSaved.getCategoryId())) {
            result.addError(new FieldError("Category", "categoryName", "deze category bestaat al."));
        }

        if (result.hasErrors()) {
            return showCategoryForm(datamodel, categoryToBeSaved);
        }
        categoryRepository.save(categoryToBeSaved);

        return ("redirect:/category/all");
    }
}
