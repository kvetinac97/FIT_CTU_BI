package wrzecond.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wrzecond.entity.TjvEntity;

public interface TjvRepository<T extends TjvEntity> extends JpaRepository<T, Integer> {

}
