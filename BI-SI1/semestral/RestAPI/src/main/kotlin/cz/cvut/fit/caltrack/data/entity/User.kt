package cz.cvut.fit.caltrack.data.entity

import java.util.*
import javax.persistence.*

@Entity
data class User (
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val password: String,
    @Column val birth: Date?,
    @Column val height: Int?,
    @Column val weight: Int?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal? = null,
    @Column val calories: Int?,
    @Column val carbohydrates: Int?,
    @Column val protein: Int?,
    @Column val fat: Int?,

    @Column(nullable = false) val email: String,
    @Column(unique = true, nullable = false) val code: String,
    override val id: Int = 0
) : IEntity(id)