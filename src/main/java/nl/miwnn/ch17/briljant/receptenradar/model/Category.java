package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * @author Johan Elzinga
 * An entity that is responsible for a category that can be added to a recipe
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    @Column(nullable=false)
    private int categoryLikes = 0;

    @Column(unique=true)
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;



}
