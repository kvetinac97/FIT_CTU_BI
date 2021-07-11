package wrzecond.service

import org.springframework.stereotype.Service
import wrzecond.*
import wrzecond.entity.TjvOrderFood
import wrzecond.entity.TjvTable
import wrzecond.repository.TjvOrderFoodRepository
import java.sql.Timestamp
import java.time.Instant

@Service
class TjvOrderFoodService (override val repository: TjvOrderFoodRepository,
                            private val orderService: TjvOrderService,
                            private val foodService : TjvFoodService,
                            private val tableService: TjvTableService)
: TjvService<TjvOrderFood, TjvOrderFoodReadDTO, TjvOrderFoodCreateDTO, TjvOrderFoodUpdateDTO> (repository) {

    fun createOrderAtTypeWithFood (type: TjvTableType, food: List<TjvOrderFoodUpdateDTO>)
        = createAt (tableService.findByType(type), food)

    fun createOrUpdateAtTable (tableId: Int, food: List<TjvOrderFoodUpdateDTO>) : TjvOrderReadDTO {
        // Is there an active order containing food on the table?
        val previous = getFoodAtTable(tableId)

        // No active order, create new
        if (previous.isEmpty())
            return createAt (listOf(tableService.getEntityByID(tableId)), food)[0]

        // Create help maps
        val orderId = previous[0].order
        val foodCount = HashMap<Int, TjvOrderFoodUpdateDTO>()
        food.forEach {
            val foodId = it.food
            if (foodId != null)
                foodCount[foodId] = it
        }

        previous.forEach {
            // Get count
            val nowCount = foodCount[it.food.id]?.count

            // Was before, is no longer - delete
            if (nowCount == null) {
                delete(it.id)
                return@forEach
            }

            // Was before with same count - don't change anything
            if (it.count == nowCount) {
                foodCount.remove(it.food.id)
                return@forEach
            }

            // Was before - different count - update count
            val updateDto = TjvOrderFoodUpdateDTO(orderId, it.food.id, it.datetime, nowCount)
            update(it.id, updateDto)
            foodCount.remove(it.food.id) // remove duplicates
        }

        // Wasn't before - create
        val dtos = foodCount.map { entry -> TjvOrderFoodCreateDTO(orderId, entry.key,
                entry.value.datetime ?: Timestamp.from(Instant.now()), entry.value.count ?: 0)
        }.toTypedArray()
        create(*dtos)

        // Return the actual order
        return orderService.getByID(orderId)
    }

    private fun createAt (tables: List<TjvTable>, food: List<TjvOrderFoodUpdateDTO>) : List<TjvOrderReadDTO> {
        // Construct order on each table
        val orders = tables.map {
            // Order exists - add to this order, doesn't exist - create new
            orderService
                .findByTableID(it.id)
                .ifEmpty { orderService.create(TjvOrderCreateDTO(it.id, Timestamp.from(Instant.now()), false)) }
                .first()
        }

        // Add requested food in each order
        orders.forEach { order ->
            val dtos = food.map {
                TjvOrderFoodCreateDTO(order.id, it.food ?: 0, it.datetime ?: Timestamp.from(Instant.now()), it.count ?: 0)
            }.toTypedArray()
            create(*dtos)
        }
        return orders
    }

    fun getFoodAtTable (tableId: Int)
    = repository.findByTable(tableService.getEntityByID(tableId))
        .map { it.toDTO() }
        .sortedWith { left, right ->
            if (left.food.cooked == right.food.cooked)
                left.food.name.compareTo(right.food.name, true)
            else left.food.cooked.compareTo(right.food.cooked)
        }

    override fun TjvOrderFood.toDTO() = TjvOrderFoodReadDTO (id, order.id, foodService.run { food.toDTO() }, datetime, count)
    override fun TjvOrderFoodCreateDTO.toEntity() = TjvOrderFood (
        orderService.getEntityByID(order),
        foodService.getEntityByID(food),
        datetime, count
    )
    override fun TjvOrderFood.merge (dto: TjvOrderFoodUpdateDTO) = TjvOrderFood (
        dto.order?.let { orderService.getEntityByID(it) } ?: order,
        dto.food?.let { foodService.getEntityByID(it) } ?: food,
        dto.datetime ?: datetime, dto.count ?: count, id
    )

}