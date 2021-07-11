package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.MicronutrientCreateDTO
import cz.cvut.fit.caltrack.business.dto.MicronutrientReadDTO
import cz.cvut.fit.caltrack.business.dto.MicronutrientUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.Micronutrient
import cz.cvut.fit.caltrack.data.repository.MicronutrientRepository
import org.springframework.stereotype.Service

@Service
class MicronutrientService (override val repository: MicronutrientRepository)
    : IServiceImpl<Micronutrient, MicronutrientReadDTO, MicronutrientCreateDTO, MicronutrientUpdateDTO>(repository) {

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun Micronutrient.toDTO () = MicronutrientReadDTO(id, name)
    override fun MicronutrientCreateDTO.toEntity (user: UserReadDTO) = Micronutrient(name)
    override fun Micronutrient.merge (dto: MicronutrientUpdateDTO, user: UserReadDTO) = Micronutrient (
        dto.name ?: name, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}