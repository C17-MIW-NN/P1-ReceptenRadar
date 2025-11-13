package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iris Loermans
 * Blueprint for writing the preperation method for a Recipe.
 */

@Entity
public class Direction {
    @Id @GeneratedValue
    private Long id;
    private List<String> steps = new ArrayList<>();

    public Direction() {}

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Direction(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(String step) {
        steps.add(step);
    }
}
