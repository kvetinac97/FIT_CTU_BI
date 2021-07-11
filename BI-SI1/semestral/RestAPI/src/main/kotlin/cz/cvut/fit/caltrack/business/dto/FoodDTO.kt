package cz.cvut.fit.caltrack.business.dto

data class FoodReadDTO (
    override val id: Int,
    val name: String,
    val unit: String,
    val unitToGrams: Double,
    val fat: Int,
    val carbohydrates: Int,
    val protein: Int
) : IReadDTO

data class FoodUpdateDTO (
    val name: String?,
    val unit: String?,
    val unitToGrams: Double?,
    val fat: Int?,
    val carbohydrates: Int?,
    val protein: Int?
) : IUpdateDTO

data class FoodCreateDTO (
    val name: String,
    val unit: String,
    val unitToGrams: Double,
    val fat: Int,
    val carbohydrates: Int,
    val protein: Int
) : ICreateDTO
