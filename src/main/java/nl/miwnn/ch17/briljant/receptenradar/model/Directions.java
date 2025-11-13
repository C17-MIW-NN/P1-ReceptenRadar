package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Iris Loermans
 * Blueprint for writing the preperation method for a Recipe.
 */

@Entity
public class Directions {
    @Id @GeneratedValue
    private Long id;
    private String description;

    public  Directions() {}

    public Directions(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
