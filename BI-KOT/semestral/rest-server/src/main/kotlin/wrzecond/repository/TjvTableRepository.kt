package wrzecond.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import wrzecond.entity.TjvTable
import wrzecond.TjvTableType

@Repository
interface TjvTableRepository : TjvRepository<TjvTable> {

    @Query("SELECT t FROM TjvTable t WHERE t.type = :type")
    fun findByType (type: TjvTableType) : List<TjvTable>

}
