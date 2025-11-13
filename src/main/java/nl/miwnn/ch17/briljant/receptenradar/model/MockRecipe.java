package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iris Loermans
 * purpose of the code
 */

@Entity
public class MockRecipe {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ElementCollection
    private List<String> steps = new ArrayList<>();

    public MockRecipe() {
        this.name = "Default naam";
        this.steps.add("");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
