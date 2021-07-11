package cz.cvut.fit.caltrack.business.dto

data class ActivityReadDTO (
    override val id: Int,
    val name: String,
    val caloriesPerMinute: Double,
    val unit: String,
    val unitToMinutes: Double
) : IReadDTO

data class ActivityUpdateDTO (
    val name: String?,
    val caloriesPerMinute: Double?,
    val unit: String?,
    val unitToMinutes: Double?
) : IUpdateDTO

data class ActivityCreateDTO (
    val name: String,
    val caloriesPerMinute: Double,
    val unit: String,
    val unitToMinutes: Double
) : ICreateDTO