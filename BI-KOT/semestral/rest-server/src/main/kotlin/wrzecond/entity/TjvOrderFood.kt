package wrzecond.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class TjvOrderFood (
    @ManyToOne
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "order_id", nullable = false)
    val order: TjvOrder,

    @ManyToOne
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "food_id", nullable = false)
    val food: TjvFood,

    @Column(nullable = false) val datetime: Timestamp,
    @Column(nullable = false) val count: Int = 0,
    override val id: Int = 0
) : TjvEntity()
