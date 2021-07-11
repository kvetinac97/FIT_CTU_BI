package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.MealCreateDTO
import cz.cvut.fit.caltrack.business.dto.MealReadDTO
import cz.cvut.fit.caltrack.business.dto.MealUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.Meal
import cz.cvut.fit.caltrack.data.repository.MealRepository
import org.springframework.stereotype.Service

@Service
class MealService (override val repository: MealRepository)
    : IServiceImpl<Meal, MealReadDTO, MealCreateDTO, MealUpdateDTO>(repository) {

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun Meal.toDTO () = MealReadDTO(id, name, timeOfDay)
    override fun MealCreateDTO.toEntity (user: UserReadDTO) = Meal(name, timeOfDay)
    override fun Meal.merge (dto: MealUpdateDTO, user: UserReadDTO) = Meal (
        dto.name ?: name, dto.timeOfDay ?: timeOfDay, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}