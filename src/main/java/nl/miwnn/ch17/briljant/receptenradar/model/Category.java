package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * @author Johan Elzinga
 * An entity that is responsible for a category that can be added to a recipe
 */

@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    @Column(unique=true)
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

}
