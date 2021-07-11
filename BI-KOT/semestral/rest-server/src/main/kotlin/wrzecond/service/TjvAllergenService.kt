package wrzecond.service

import org.springframework.stereotype.Service
import wrzecond.TjvAllergenCreateDTO
import wrzecond.TjvAllergenReadDTO
import wrzecond.TjvAllergenUpdateDTO
import wrzecond.entity.TjvAllergen
import wrzecond.repository.TjvAllergenRepository

@Service
class TjvAllergenService (override val repository: TjvAllergenRepository)
: TjvService<TjvAllergen, TjvAllergenReadDTO, TjvAllergenCreateDTO, TjvAllergenUpdateDTO> (repository) {

    override fun TjvAllergen.toDTO() = TjvAllergenReadDTO(id, name)
    override fun TjvAllergenCreateDTO.toEntity() = TjvAllergen(name)
    override fun TjvAllergen.merge (dto: TjvAllergenUpdateDTO) = TjvAllergen(dto.name ?: name, id)

}