package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Douwe Jan Hamersma
 * Linking entity between Recipe, Ingredient and Unit.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecipeIngredient {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Double quantity;
    private String unit;

}
