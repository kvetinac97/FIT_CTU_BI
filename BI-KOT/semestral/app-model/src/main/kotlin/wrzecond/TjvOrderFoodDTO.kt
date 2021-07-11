package wrzecond

import java.sql.Timestamp

data class TjvOrderFoodReadDTO (
    override val id: Int,
    val order: Int,
    val food: TjvFoodReadDTO,
    val datetime: Timestamp,
    val count: Int
) : IReadDTO { override val displayName = "$order - ${food.displayName}" }

data class TjvOrderFoodCreateDTO (
    val order: Int,
    val food: Int,
    val datetime: Timestamp,
    val count: Int
) : ICreateDTO

data class TjvOrderFoodUpdateDTO (
    val order: Int? = null,
    val food: Int? = null,
    val datetime: Timestamp? = null,
    val count: Int? = null
) : IUpdateDTO