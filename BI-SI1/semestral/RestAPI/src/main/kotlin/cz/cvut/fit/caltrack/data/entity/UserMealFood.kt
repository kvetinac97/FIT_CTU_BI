package cz.cvut.fit.caltrack.data.entity

import java.sql.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class UserMealFood (
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    val meal: Meal,

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

    @Column(nullable = false) val date: Date,
    @Column(nullable = false) val amount: Int,
    override val id: Int = 0
) : IEntity(id)