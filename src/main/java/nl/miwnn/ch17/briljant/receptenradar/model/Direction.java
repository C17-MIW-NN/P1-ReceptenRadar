package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;

/**
 * @author Iris Loermans
 * The concept for the directions belonging to a Recipe
 */

@Entity
public class Direction {
    @Id @GeneratedValue
    private long id;
    private int directionNumber;
    private String direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDirectionNumber() {
        return directionNumber;
    }

    public void setDirectionNumber(int directionNumber) {
        this.directionNumber = directionNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
