package wrzecond.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class TjvAllergen (
    @Column(nullable = false) val name: String,
    override val id: Int = 0
) : TjvEntity()
