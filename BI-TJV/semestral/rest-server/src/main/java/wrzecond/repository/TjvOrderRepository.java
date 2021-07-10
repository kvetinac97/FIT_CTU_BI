package wrzecond.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wrzecond.entity.TjvOrder;
import wrzecond.entity.TjvTable;

import java.util.List;

@Repository
public interface TjvOrderRepository extends TjvRepository<TjvOrder> {

    @Query("SELECT o FROM TjvOrder o WHERE o.table = :table AND o.completed = 0")
    List<TjvOrder> findByTableID (TjvTable table);

}
