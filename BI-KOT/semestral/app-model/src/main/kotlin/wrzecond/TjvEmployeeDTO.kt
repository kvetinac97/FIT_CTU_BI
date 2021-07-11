package wrzecond

data class TjvEmployeeReadDTO (
    override val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val admin: Boolean
) : IReadDTO { override val displayName = "$firstName $lastName" }

data class TjvEmployeeCreateDTO (
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val admin: Boolean
) : ICreateDTO

data class TjvEmployeeUpdateDTO (
    val username: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val admin: Boolean? = null
) : IUpdateDTO

data class TjvPasswordDTO (
    val old: String = "",
    val new: String = ""
)