package wrzecond.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import wrzecond.entity.TjvOrderFood
import wrzecond.entity.TjvTable

@Repository
interface TjvOrderFoodRepository : TjvRepository<TjvOrderFood> {

    @Query("SELECT orf FROM TjvOrderFood orf JOIN orf.order o WHERE o.table = :table AND o.completed = false")
    fun findByTable (table: TjvTable) : List<TjvOrderFood>

}
