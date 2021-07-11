package wrzecond

enum class TjvTableType { BAR, INSIDE, OUTSIDE }

data class TjvTableReadDTO (override val id: Int, val type: TjvTableType) : IReadDTO {
    override val displayName = type.name
    var empty: Boolean = false
}

data class TjvTableCreateDTO (val type: TjvTableType)  : ICreateDTO
data class TjvTableUpdateDTO (val type: TjvTableType? = null) : IUpdateDTO