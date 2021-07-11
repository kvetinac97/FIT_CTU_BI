package wrzecond.entity

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class TjvEntity (
    @Id
    @GeneratedValue
    open val id: Int = 0
)
