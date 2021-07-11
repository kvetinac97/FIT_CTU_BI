package wrzecond.repository

import org.springframework.data.jpa.repository.JpaRepository
import wrzecond.entity.TjvEntity

interface TjvRepository<T : TjvEntity> : JpaRepository<T, Int>