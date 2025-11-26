package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iris Loermans
 * The concept for the directions belonging to a Recipe
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Direction {
    @Id @GeneratedValue
    private long id;
    private int directionNumber;
    private String direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;


}
