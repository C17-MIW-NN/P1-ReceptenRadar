package nl.miwnn.ch17.briljant.receptenradar.repositories;

import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Johan Elzinga
 */

public interface CategoryRepository  extends JpaRepository<Category, Long> {
}
