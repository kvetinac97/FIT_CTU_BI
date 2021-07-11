package cz.cvut.fit.caltrack.data.entity

import javax.persistence.*

@Entity
data class FoodMicronutrient (
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

    @ManyToOne
    @JoinColumn(name = "micronutrient_id", nullable = false)
    val micronutrient: Micronutrient,

    @Column(nullable = false) val amount: Int,
    override val id: Int = 0
) : IEntity(id)