package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.FoodCreateDTO
import cz.cvut.fit.caltrack.business.dto.FoodReadDTO
import cz.cvut.fit.caltrack.business.dto.FoodUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.Food
import cz.cvut.fit.caltrack.data.repository.FoodRepository
import org.springframework.stereotype.Service

@Service
class FoodService (override val repository: FoodRepository)
: IServiceImpl<Food, FoodReadDTO, FoodCreateDTO, FoodUpdateDTO>(repository) {

    /**
     * Search for food matching given name
     * @param name name of food being searched for
     * @return list of food matching given name
     */
    fun searchByName (name: String) = repository.searchByName(name).take(IService.PAGE_SIZE).map { it.toDTO() }

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun Food.toDTO () = FoodReadDTO(id, name, unit, unitToGrams, fat, carbohydrates, protein)
    override fun FoodCreateDTO.toEntity (user: UserReadDTO) = Food(name, unit, unitToGrams, fat, carbohydrates, protein)
    override fun Food.merge (dto: FoodUpdateDTO, user: UserReadDTO) = Food (
        dto.name ?: name,
        dto.unit ?: unit,
        dto.unitToGrams ?: unitToGrams,
        dto.fat ?: fat,
        dto.carbohydrates ?: carbohydrates,
        dto.protein ?: protein,
        id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}