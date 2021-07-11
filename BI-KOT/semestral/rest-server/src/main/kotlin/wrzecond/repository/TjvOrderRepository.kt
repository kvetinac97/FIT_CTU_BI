package wrzecond.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import wrzecond.entity.TjvOrder
import wrzecond.entity.TjvTable

@Repository
interface TjvOrderRepository : TjvRepository<TjvOrder> {

    @Query("SELECT o FROM TjvOrder o WHERE o.table = :table AND o.completed = false")
    fun findByTableID (table: TjvTable) : List<TjvOrder>

}
