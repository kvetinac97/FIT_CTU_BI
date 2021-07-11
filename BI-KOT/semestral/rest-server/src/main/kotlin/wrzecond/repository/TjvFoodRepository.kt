package wrzecond.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import wrzecond.entity.TjvAllergen
import wrzecond.entity.TjvFood

@Repository
interface TjvFoodRepository : TjvRepository<TjvFood> {

    @Query("SELECT allergen FROM TjvFood food JOIN food.allergens allergen WHERE food = :food")
    fun getAllergensInFood (food: TjvFood) : List<TjvAllergen>

}
