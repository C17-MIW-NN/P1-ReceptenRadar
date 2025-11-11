package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;

import java.util.Set;

/**
 * @author Douwe Jan Hamersma
 * The concept of a recipe.
 */

@Entity
public class Recipe {

    @Id @GeneratedValue
    private Long recipeId;

    @Column(unique = true)
    private String recipeName;

    private int preparationTime;

    private int forAmountOfPeople;

    private int calories;

    private String coverImageUrl;

    @ManyToMany
    @JoinTable(
            joinColumns = { @JoinColumn(name = "recipeId") },
            inverseJoinColumns = { @JoinColumn(name = "ingredientId") }
    )
    private Set<Ingredient> ingredients;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getForAmountOfPeople() {
        return forAmountOfPeople;
    }

    public void setForAmountOfPeople(int forAmountOfPeople) {
        this.forAmountOfPeople = forAmountOfPeople;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preperationTime) {
        this.preparationTime = preperationTime;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
