package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.GoalReadDTO
import cz.cvut.fit.caltrack.business.dto.UserCreateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.business.dto.UserUpdateDTO
import cz.cvut.fit.caltrack.data.entity.Goal
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.data.repository.GoalRepository
import cz.cvut.fit.caltrack.data.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserService (override val repository: UserRepository, private val goalRepository: GoalRepository)
    : IServiceImpl<User, UserReadDTO, UserCreateDTO, UserUpdateDTO>(repository) {

    /**
     * Gets the UserReadDTO with given code
     * @param code code of searched user
     * @return read DTO of given user
     */
    fun getByCode (code: String) : UserReadDTO
        = repository.getByCode(code).toDTO()

    /**
     * Helper extension function to convert Goal to GoalReadDTO
     * @return GoalReadDTO reflecting the Food
     */
    fun Goal.toDTO () = GoalReadDTO(id, name, coefficient)

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun User.toDTO () = UserReadDTO(id, birth, height, weight, goal?.toDTO(), calories, carbohydrates, protein, fat, code)
    override fun UserCreateDTO.toEntity (user: UserReadDTO) : User {
        val userEntity = repository.getOne(user.id)
        val goal = goalRepository.getOne(goal)
        val age = TimeUnit.DAYS.convert(System.currentTimeMillis() - birth.time, TimeUnit.MILLISECONDS) / 365

        // Calculates 1st recommendation for user based on weight, height...
        val calories = goal.coefficient * ( 10 * weight.toDouble() + 6.25 * height.toDouble() - 5 * age.toDouble() + 5 )
        val carbohydrates = calories * 0.5
        val fat = calories * 0.3
        val protein = calories * 0.2

        return User (userEntity.name, userEntity.password, birth, height, weight,
            goal, calories.toInt(), carbohydrates.toInt(), protein.toInt(), fat.toInt(),
            userEntity.email, userEntity.code, userEntity.id)
    }
    override fun User.merge (dto: UserUpdateDTO, user: UserReadDTO) = User (
        name, password, birth, height, weight, goal,
        dto.calories ?: calories,
        dto.carbohydrates ?: carbohydrates,
        dto.protein ?: protein,
        dto.fat ?: fat,
        email, code, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}