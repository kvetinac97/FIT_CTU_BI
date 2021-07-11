package cz.cvut.fit.caltrack.data.entity

import java.sql.Time
import javax.persistence.*

@Entity
data class Meal (
    @Column(nullable = false) val name: String,
    @Column(name="time_of_day", nullable = false) val timeOfDay: Time,
    override val id: Int = 0
) : IEntity(id)