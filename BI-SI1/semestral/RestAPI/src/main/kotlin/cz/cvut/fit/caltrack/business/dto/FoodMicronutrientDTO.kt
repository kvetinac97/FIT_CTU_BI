package cz.cvut.fit.caltrack.business.dto

data class FoodMicronutrientReadDTO (
    override val id: Int,
    val food: FoodReadDTO,
    val micronutrient: MicronutrientReadDTO,
    val amount: Int
) : IReadDTO

data class FoodMicronutrientUpdateDTO (
    val foodId: Int?,
    val micronutrientId: Int?,
    val amount: Int?
) : IUpdateDTO

data class FoodMicronutrientCreateDTO (
    val foodId: Int,
    val micronutrientId: Int,
    val amount: Int
) : ICreateDTO