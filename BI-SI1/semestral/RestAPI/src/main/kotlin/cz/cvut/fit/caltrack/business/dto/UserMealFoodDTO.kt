package cz.cvut.fit.caltrack.business.dto

import java.sql.Date

data class UserMealFoodReadDTO (
    override val id: Int,
    val meal: MealReadDTO,
    val food: FoodReadDTO,
    val date: Date,
    val amount: Int
) : IReadDTO

data class UserMealFoodUpdateDTO (
    val mealId: Int?,
    val foodId: Int?,
    val date: Date?,
    val amount: Int?
) : IUpdateDTO

data class UserMealFoodCreateDTO (
    val mealId: Int,
    val foodId: Int,
    val date: Date?,
    val amount: Int
) : ICreateDTO