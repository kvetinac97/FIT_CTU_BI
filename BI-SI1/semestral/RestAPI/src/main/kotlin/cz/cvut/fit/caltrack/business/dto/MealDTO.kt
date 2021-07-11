package cz.cvut.fit.caltrack.business.dto

import java.sql.Time

data class MealReadDTO (override val id: Int, val name: String, val timeOfDay: Time) : IReadDTO
data class MealUpdateDTO (val name: String?, val timeOfDay: Time?) : IUpdateDTO
data class MealCreateDTO (val name: String, val timeOfDay: Time) : ICreateDTO