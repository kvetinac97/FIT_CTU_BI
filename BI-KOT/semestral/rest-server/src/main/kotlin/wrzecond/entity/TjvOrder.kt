package wrzecond.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class TjvOrder(
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "table_id", nullable = false)
    val table: TjvTable,

    @Column(nullable = false) val datetime: Timestamp,
    @Column(nullable = false) val completed: Boolean,
    override val id: Int = 0
) : TjvEntity()
