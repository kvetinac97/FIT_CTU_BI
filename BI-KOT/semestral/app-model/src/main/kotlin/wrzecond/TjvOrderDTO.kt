package wrzecond

import java.sql.Timestamp

data class TjvOrderReadDTO (
    override val id: Int,
    val table: Int,
    val datetime: Timestamp,
    val completed: Boolean
) : IReadDTO { override val displayName = "$id" }

data class TjvOrderCreateDTO (
    val table: Int,
    val datetime: Timestamp,
    val completed: Boolean = false
) : ICreateDTO

data class TjvOrderUpdateDTO (
    val table: Int? = null,
    val datetime: Timestamp? = null,
    val completed: Boolean? = null
) : IUpdateDTO