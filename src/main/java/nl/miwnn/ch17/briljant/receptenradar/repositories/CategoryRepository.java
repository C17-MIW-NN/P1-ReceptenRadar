package nl.miwnn.ch17.briljant.receptenradar.repositories;
import nl.miwnn.ch17.briljant.receptenradar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Johan Elzinga
 */

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName (String categoryName);
    List<Category> findAllByOrderByCategoryLikesDesc();
}
