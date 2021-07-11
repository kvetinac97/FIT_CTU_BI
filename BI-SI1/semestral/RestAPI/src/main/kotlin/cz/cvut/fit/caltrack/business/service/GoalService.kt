package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.GoalCreateDTO
import cz.cvut.fit.caltrack.business.dto.GoalReadDTO
import cz.cvut.fit.caltrack.business.dto.GoalUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.Goal
import cz.cvut.fit.caltrack.data.repository.GoalRepository
import org.springframework.stereotype.Service

@Service
class GoalService (override val repository: GoalRepository)
    : IServiceImpl<Goal, GoalReadDTO, GoalCreateDTO, GoalUpdateDTO>(repository) {

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun Goal.toDTO () = GoalReadDTO(id, name, coefficient)
    override fun GoalCreateDTO.toEntity (user: UserReadDTO) = Goal(name, coefficient)
    override fun Goal.merge (dto: GoalUpdateDTO, user: UserReadDTO) = Goal (
        dto.name ?: name,
        dto.coefficient ?: coefficient,
        id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}