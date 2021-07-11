package cz.cvut.fit.caltrack.data.entity

import javax.persistence.*

@Entity
data class Food (
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val unit: String,
    @Column(name="unit_to_grams", nullable = false) val unitToGrams: Double,
    @Column(nullable = false) val fat: Int,
    @Column(nullable = false) val carbohydrates: Int,
    @Column(nullable = false) val protein: Int,
    override val id: Int = 0
) : IEntity(id)