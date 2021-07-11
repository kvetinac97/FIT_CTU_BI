package wrzecond.entity

import wrzecond.TjvTableType
import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class TjvTable (
    @Column(nullable = false) val type: TjvTableType,
    override val id: Int = 0
) : TjvEntity()
