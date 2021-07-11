package wrzecond.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import wrzecond.TjvTableCreateDTO
import wrzecond.TjvTableReadDTO
import wrzecond.TjvTableType
import wrzecond.TjvTableUpdateDTO
import wrzecond.entity.TjvTable
import wrzecond.repository.TjvTableRepository

@Service
class TjvTableService (override val repository: TjvTableRepository)
: TjvService<TjvTable, TjvTableReadDTO, TjvTableCreateDTO, TjvTableUpdateDTO>(repository) {

    override val sort: Sort
        get() = Sort.by(Sort.Direction.ASC, "id")

    fun findByType (type: TjvTableType) = repository.findByType(type)

    override fun TjvTable.toDTO() = TjvTableReadDTO (id, type)
    override fun TjvTableCreateDTO.toEntity() = TjvTable(type)
    override fun TjvTable.merge (dto: TjvTableUpdateDTO) = TjvTable(dto.type ?: type, id)

}