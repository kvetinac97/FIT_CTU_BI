package wrzecond.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wrzecond.entity.TjvAllergen;
import wrzecond.entity.TjvFood;

import java.util.List;

@Repository
public interface TjvFoodRepository extends TjvRepository<TjvFood> {

    @Query("SELECT allergen FROM TjvFood food JOIN food.allergens allergen WHERE food = :food")
    List<TjvAllergen> getAllergensInFood (TjvFood food);

}
