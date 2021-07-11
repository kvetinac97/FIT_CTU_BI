package cz.cvut.fit.caltrack.business.dto

data class MicronutrientReadDTO   (override val id: Int, val name: String) : IReadDTO
data class MicronutrientUpdateDTO (val name: String?) : IUpdateDTO
data class MicronutrientCreateDTO (val name: String) : ICreateDTO