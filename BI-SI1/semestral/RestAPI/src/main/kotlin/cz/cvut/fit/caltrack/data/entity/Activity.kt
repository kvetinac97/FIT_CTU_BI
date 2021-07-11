package cz.cvut.fit.caltrack.data.entity

import javax.persistence.*

@Entity
data class Activity (
    @Column(nullable = false) val name: String,
    @Column(name = "calories_per_minute", nullable = false) val caloriesPerMinute: Double,
    @Column(nullable = false) val unit: String,
    @Column(name="unit_to_minutes", nullable = false) val unitToMinutes: Double,
    override val id: Int = 0
) : IEntity(id)