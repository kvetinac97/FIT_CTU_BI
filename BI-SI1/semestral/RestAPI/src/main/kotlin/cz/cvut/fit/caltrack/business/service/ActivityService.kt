package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.*
import cz.cvut.fit.caltrack.data.repository.*
import org.springframework.stereotype.Service

@Service
class ActivityService (override val repository: ActivityRepository)
    : IServiceImpl<Activity, ActivityReadDTO, ActivityCreateDTO, ActivityUpdateDTO>(repository) {

    /**
     * Search for activities matching given name
     * @param name name of activity being searched for
     * @return list of activities matching given name
     */
    fun searchByName (name: String) : List<ActivityReadDTO>
        = repository.searchByName(name).take(IService.PAGE_SIZE).map { it.toDTO() }

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun Activity.toDTO () = ActivityReadDTO(id, name, caloriesPerMinute, unit, unitToMinutes)
    override fun ActivityCreateDTO.toEntity (user: UserReadDTO) = Activity(name, caloriesPerMinute, unit, unitToMinutes)
    override fun Activity.merge (dto: ActivityUpdateDTO, user: UserReadDTO) = Activity (
        dto.name ?: name,
        dto.caloriesPerMinute ?: caloriesPerMinute,
        dto.unit ?: unit,
        dto.unitToMinutes ?: unitToMinutes, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}
