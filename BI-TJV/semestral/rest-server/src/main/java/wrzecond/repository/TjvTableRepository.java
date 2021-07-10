package wrzecond.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wrzecond.entity.TjvTable;
import wrzecond.table.TjvTableType;

import java.util.List;

@Repository
public interface TjvTableRepository extends TjvRepository<TjvTable> {

    @Query("SELECT t FROM TjvTable t WHERE t.type = :type")
    List<TjvTable> findByType (TjvTableType type);

}
