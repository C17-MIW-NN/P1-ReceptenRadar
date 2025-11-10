package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Douwe Jan Hamersma
 * An object of which a recipe consists.
 */

@Entity
public class Ingredient {

    @Id @GeneratedValue
    private Long ingredientId;

    @Column(unique = true)
    private String ingredientName;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
