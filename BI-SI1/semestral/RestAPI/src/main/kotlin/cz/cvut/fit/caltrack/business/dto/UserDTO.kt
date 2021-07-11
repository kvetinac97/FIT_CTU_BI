package cz.cvut.fit.caltrack.business.dto

import java.util.*

data class UserReadDTO (
    override val id: Int,
    val birth: Date?,
    val height: Int?,
    val weight: Int?,
    val goal: GoalReadDTO?,
    val calories: Int?,
    val carbohydrates: Int?,
    val protein: Int?,
    val fat: Int?,
    val code: String
) : IReadDTO

data class UserUpdateDTO (
    val calories: Int?,
    val carbohydrates: Int?,
    val protein: Int?,
    val fat: Int?,
) : IUpdateDTO

data class UserCreateDTO (
    val birth: Date,
    val height: Int,
    val weight: Int,
    val goal: Int
) : ICreateDTO