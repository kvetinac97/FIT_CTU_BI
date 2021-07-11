package cz.cvut.fit.caltrack.data.entity

import javax.persistence.*

@Entity
data class Goal (
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val coefficient: Double, // tímto koeficientem se přenásobí vypočítaný optimální příjem z hmotnosti+výšky+věku
    override val id: Int = 0
) : IEntity(id)