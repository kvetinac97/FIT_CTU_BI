package wrzecond.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wrzecond.entity.TjvOrderFood;
import wrzecond.entity.TjvTable;

import java.util.List;

@Repository
public interface TjvOrderFoodRepository extends TjvRepository<TjvOrderFood> {

    @Query("SELECT orf FROM TjvOrderFood orf JOIN orf.order o WHERE o.table = :table AND o.completed = 0")
    List<TjvOrderFood> findByTable (TjvTable table);

}
