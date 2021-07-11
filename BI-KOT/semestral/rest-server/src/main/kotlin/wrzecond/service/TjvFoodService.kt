package wrzecond.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import wrzecond.TjvFoodCreateDTO
import wrzecond.TjvFoodReadDTO
import wrzecond.TjvFoodUpdateDTO
import wrzecond.entity.TjvFood
import wrzecond.repository.TjvFoodRepository

@Service
class TjvFoodService (override val repository: TjvFoodRepository, private val allergenService: TjvAllergenService)
: TjvService<TjvFood, TjvFoodReadDTO, TjvFoodCreateDTO, TjvFoodUpdateDTO>(repository) {

    override val sort: Sort
        get() = Sort.by(Sort.Direction.ASC, "cooked", "name")

    fun getAllergensInFood (foodId: Int)
        = repository.getAllergensInFood(getEntityByID(foodId)).map { allergenService.run { it.toDTO() } }

    override fun delete (id: Int) {
        // Allow food deletion
        val food = getEntityByID(id)
        food.merge(TjvFoodUpdateDTO(allergens = emptyList()))

        repository.saveAndFlush(food)
        super.delete(id)
    }

    override fun TjvFood.toDTO() = TjvFoodReadDTO (id, name, price, cooked, allergens.map { it.id })
    override fun TjvFoodCreateDTO.toEntity() = TjvFood (name, price, cooked, allergenService.findByIDs(allergens))
    override fun TjvFood.merge (dto: TjvFoodUpdateDTO) = TjvFood (
        dto.name ?: name,
        dto.price ?: price,
        dto.cooked ?: cooked,
        dto.allergens?.let { allergenService.findByIDs(it) } ?: allergens,
        id
    )

}