package wrzecond

data class TjvAllergenReadDTO (override val id: Int, val name: String) : IReadDTO {
    override val displayName = name
}

data class TjvAllergenCreateDTO (val name: String)  : ICreateDTO
data class TjvAllergenUpdateDTO (val name: String? = null) : IUpdateDTO