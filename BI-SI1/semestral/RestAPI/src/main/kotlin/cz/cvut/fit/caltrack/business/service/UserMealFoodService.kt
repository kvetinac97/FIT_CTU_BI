package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.Food
import cz.cvut.fit.caltrack.data.entity.Meal
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.data.entity.UserMealFood
import cz.cvut.fit.caltrack.data.repository.FoodRepository
import cz.cvut.fit.caltrack.data.repository.MealRepository
import cz.cvut.fit.caltrack.data.repository.UserMealFoodRepository
import cz.cvut.fit.caltrack.data.repository.UserRepository
import org.springframework.stereotype.Service
import java.sql.Date

@Service
class UserMealFoodService (override val repository: UserMealFoodRepository, val userRepository: UserRepository, val mealRepository: MealRepository, val foodRepository: FoodRepository)
    : IServiceImpl<UserMealFood, UserMealFoodReadDTO, UserMealFoodCreateDTO, UserMealFoodUpdateDTO>(repository) {

    /**
     *  Finds all food user has ever eaten, ordered by frequency
     *  @param user user who has eaten the food
     *  @return list of FoodReadDTOs of food given user ate
     */
    fun getHistory (user: UserReadDTO) : List<FoodReadDTO>
        = repository.getHistory(userRepository.getOne(user.id)).take(IService.PAGE_SIZE).map { it.toDTO() }

    /**
     *  Finds all food user ate on given day
     *  @param user user who has ate the food
     *  @param day day on which the food was eaten
     *  @return list of UserMealFoodDTOs of food given user ate on given day in ascending order by meal (breakfast -> lunch -> dinner)
     */
    fun getDaily   (user: UserReadDTO, day: Date) : List<UserMealFoodReadDTO>
        = repository.getDaily(userRepository.getOne(user.id), day).map { it.toDTO() }

    /**
     * Helper extension function to convert Meal to MealReadDTO
     * @return MealReadDTO reflecting the Meal
     */
    fun Meal.toDTO() = MealReadDTO(id, name, timeOfDay)

    /**
     * Helper extension function to convert Food to FoodReadDTO
     * @return FoodReadDTO reflecting the Food
     */
    fun Food.toDTO() = FoodReadDTO(id, name, unit, unitToGrams, fat, carbohydrates, protein)

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun UserMealFood.toDTO() = UserMealFoodReadDTO (id, meal.toDTO(), food.toDTO(), date, amount)
    override fun UserMealFoodCreateDTO.toEntity(user: UserReadDTO) = UserMealFood (
        userRepository.getOne(user.id),
        mealRepository.getOne(mealId),
        foodRepository.getOne(foodId),
        date ?: Date(System.currentTimeMillis()), amount
    )
    override fun UserMealFood.merge (dto: UserMealFoodUpdateDTO, user: UserReadDTO) = UserMealFood (
        userRepository.getOne(user.id),
        dto.mealId?.let { mealRepository.getOne(it) } ?: meal,
        dto.foodId?.let { foodRepository.getOne(it) } ?: food,
        dto.date ?: date, dto.amount ?: amount
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}