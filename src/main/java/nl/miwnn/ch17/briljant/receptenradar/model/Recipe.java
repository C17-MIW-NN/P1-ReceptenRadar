package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Douwe Jan Hamersma
 * The concept of a recipe.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Recipe {

    @Id @GeneratedValue
    private Long recipeId;

    @Column(unique = true)
    private String recipeName;

    @ManyToMany(mappedBy = "likedRecipes")
    private Set<ReceptenRadarUser> recipeLikes;

    private int preparationTime;

    private int forAmountOfPeople;

    private int calories;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String recipeDescription;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direction>directions = new ArrayList<Direction>();


}
