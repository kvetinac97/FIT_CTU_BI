package cz.cvut.fit.caltrack.data.entity

import java.sql.Date
import javax.persistence.*

@Entity
data class UserActivity (
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    val activity: Activity,

    @Column(nullable = false) val date: Date,
    @Column(nullable = false) val duration: Int,
    override val id: Int = 0
) : IEntity(id)