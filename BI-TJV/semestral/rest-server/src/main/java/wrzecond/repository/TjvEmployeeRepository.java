package wrzecond.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wrzecond.entity.TjvEmployee;

@Repository
public interface TjvEmployeeRepository extends TjvRepository<TjvEmployee> {

    @Query("SELECT e FROM TjvEmployee e WHERE e.username = :username AND e.password = :password")
    TjvEmployee getByUsernamePassword (String username, String password);

}
