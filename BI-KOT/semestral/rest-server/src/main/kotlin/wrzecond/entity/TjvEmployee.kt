package wrzecond.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class TjvEmployee (
    @Column(nullable = false) val username: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) val firstName: String,
    @Column(nullable = false) val lastName: String,
    @Column(nullable = false) val admin: Boolean,
    override val id: Int = 0
) : TjvEntity()
