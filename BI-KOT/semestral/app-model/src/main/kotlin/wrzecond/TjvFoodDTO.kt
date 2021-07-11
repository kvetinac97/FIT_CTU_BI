package wrzecond

data class TjvFoodReadDTO (
    override val id: Int,
    val name: String,
    val price: Int,
    val cooked: Boolean,
    val allergens: List<Int>
) : IReadDTO { override val displayName = name }

data class TjvFoodCreateDTO (
    val name: String,
    val price: Int,
    val cooked: Boolean,
    val allergens: List<Int>
) : ICreateDTO

data class TjvFoodUpdateDTO (
    val name: String? = null,
    val price: Int? = null,
    val cooked: Boolean? = null,
    val allergens: List<Int>? = null
) : IUpdateDTO