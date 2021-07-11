package cz.cvut.fit.caltrack.business.dto

data class GoalReadDTO (override val id: Int, val name: String, val coefficient: Double) : IReadDTO
data class GoalUpdateDTO (val name: String?, val coefficient: Double?) : IUpdateDTO
data class GoalCreateDTO (val name: String, val coefficient: Double) : ICreateDTO