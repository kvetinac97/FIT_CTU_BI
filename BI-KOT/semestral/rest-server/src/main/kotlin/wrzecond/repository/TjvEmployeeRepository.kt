package wrzecond.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import wrzecond.entity.TjvEmployee

@Repository
interface TjvEmployeeRepository : TjvRepository<TjvEmployee> {

    @Query("SELECT e FROM TjvEmployee e WHERE e.username = :username AND e.password = :password")
    fun getByUsernamePassword (username: String, password: String) : TjvEmployee

}
