package wrzecond.service

import org.springframework.stereotype.Service
import wrzecond.TjvOrderCreateDTO
import wrzecond.TjvOrderReadDTO
import wrzecond.TjvOrderUpdateDTO
import wrzecond.entity.TjvOrder
import wrzecond.repository.TjvOrderRepository

@Service
class TjvOrderService (override val repository: TjvOrderRepository, private val tableService: TjvTableService)
: TjvService<TjvOrder, TjvOrderReadDTO, TjvOrderCreateDTO, TjvOrderUpdateDTO>(repository) {

    fun findByTableID (tableId: Int)
        = repository.findByTableID(tableService.getEntityByID(tableId)).map { it.toDTO() }

    override fun TjvOrder.toDTO() = TjvOrderReadDTO (id, table.id, datetime, completed)
    override fun TjvOrderCreateDTO.toEntity() = TjvOrder (tableService.getEntityByID(table), datetime, completed)
    override fun TjvOrder.merge (dto: TjvOrderUpdateDTO) = TjvOrder (
        dto.table?.let { tableService.getEntityByID(it) } ?: table,
        dto.datetime ?: datetime,
        dto.completed ?: completed,
        id
    )

}