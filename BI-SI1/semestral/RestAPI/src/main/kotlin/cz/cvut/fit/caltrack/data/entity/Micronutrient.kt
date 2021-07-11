package cz.cvut.fit.caltrack.data.entity

import javax.persistence.*

@Entity
data class Micronutrient (
    @Column(nullable = false) val name: String,
    override val id: Int = 0
) : IEntity(id)