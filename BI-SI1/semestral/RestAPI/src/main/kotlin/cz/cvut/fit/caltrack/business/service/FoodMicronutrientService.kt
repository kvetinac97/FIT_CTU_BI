package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.*
import cz.cvut.fit.caltrack.data.repository.*
import org.springframework.stereotype.Service

@Service
class FoodMicronutrientService (override val repository: FoodMicronutrientRepository, val foodRepository: FoodRepository, val micronutrientRepository: MicronutrientRepository)
    : IServiceImpl<FoodMicronutrient, FoodMicronutrientReadDTO, FoodMicronutrientCreateDTO, FoodMicronutrientUpdateDTO>(repository) {

    /**
     * Helper extension function to convert Food to FoodReadDTO
     * @return FoodReadDTO reflecting the Food
     */
    fun Food.toDTO() = FoodReadDTO(id, name, unit, unitToGrams, fat, carbohydrates, protein)

    /**
     * Helper extension function to convert Micronutrient to MicronutrientReadDTO
     * @return MicronutrientReadDTO reflecting the Micronutrient
     */
    fun Micronutrient.toDTO() = MicronutrientReadDTO(id, name)

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun FoodMicronutrient.toDTO()
        = FoodMicronutrientReadDTO (id, food.toDTO(), micronutrient.toDTO(), amount)

    override fun FoodMicronutrientCreateDTO.toEntity (user: UserReadDTO) = FoodMicronutrient (
        foodRepository.getOne(foodId),
        micronutrientRepository.getOne(micronutrientId),
        amount
    )

    override fun FoodMicronutrient.merge (dto: FoodMicronutrientUpdateDTO, user: UserReadDTO) = FoodMicronutrient (
        dto.foodId?.let { foodRepository.getOne(it) } ?: food,
        dto.micronutrientId?.let { micronutrientRepository.getOne(it) } ?: micronutrient,
        dto.amount ?: amount, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}
