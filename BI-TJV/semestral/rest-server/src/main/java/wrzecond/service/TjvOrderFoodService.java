package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order_food.TjvOrderFoodCreateDTO;
import wrzecond.order_food.TjvOrderFoodReadDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.TjvOrderFood;
import wrzecond.entity.TjvTable;
import wrzecond.repository.TjvOrderFoodRepository;
import wrzecond.table.TjvTableType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TjvOrderFoodService extends TjvService<TjvOrderFood, TjvOrderFoodReadDTO, TjvOrderFoodCreateDTO> {

    // === PARAMETERS ===
    private final TjvOrderFoodRepository repository;
    private final TjvOrderService orderService;
    private final TjvFoodService foodService;
    private final TjvTableService tableService;

    @Autowired
    public TjvOrderFoodService (TjvOrderFoodRepository repository, TjvOrderService orderService,
                                TjvFoodService foodService, TjvTableService tableService) {
        super (repository);
        this.repository = repository;
        this.orderService = orderService;
        this.foodService = foodService;
        this.tableService = tableService;
    }

    public List<TjvOrderReadDTO> createOrderAtTypeWithFood (TjvTableType type, List<TjvOrderFoodCreateDTO> food) {
        return createAt(tableService.findByType(type), food);
    }
    public TjvOrderReadDTO createOrUpdateAtTable (int tableId, List<TjvOrderFoodCreateDTO> food) {
        // Is there an active order containing food on the table?
        List<TjvOrderFoodReadDTO> previous = getFoodAtTable(tableId);

        // No active order, create new
        if ( previous.isEmpty() )
            return createAt(Collections.singletonList(tableService.getEntityByID(tableId)), food).get(0);

        // Create help maps
        int orderId = previous.get(0).getOrder();
        HashMap<Integer, TjvOrderFoodCreateDTO> foodCount = new HashMap<>();
        food.forEach( dto -> foodCount.put(dto.getFood(), dto) );

        previous.forEach( dto -> {
            // Was before, is no longer - delete
            if (!foodCount.containsKey(dto.getFood().getId())) {
                this.delete(dto.getId());
                return;
            }

            // Was before with same count - don't change anything
            int nowCount = foodCount.get(dto.getFood().getId()).getCount();
            if ( dto.getCount() == nowCount ) {
                foodCount.remove(dto.getFood().getId());
                return;
            }

            // Was before - different count - update count
            TjvOrderFoodCreateDTO updateDto = new TjvOrderFoodCreateDTO(orderId, dto.getFood().getId(), dto.getDatetime(), nowCount);
            this.update(dto.getId(), updateDto);
            foodCount.remove(dto.getFood().getId()); // remove duplicates
        } );

        // Wasn't before - create
        this.create ( foodCount.keySet()
            .stream()
            .map( foodId -> new TjvOrderFoodCreateDTO(orderId, foodId, foodCount.get(foodId).getDatetime() == null ?
                Timestamp.from(Instant.now()) : foodCount.get(foodId).getDatetime(), foodCount.get(foodId).getCount()) )
            .collect(Collectors.toList()) );

        // Return the actual order
        return orderService.getByID(orderId);
    }
    private List<TjvOrderReadDTO> createAt (List<TjvTable> tables, List<TjvOrderFoodCreateDTO> food) {
        // Construct order on each table
        List<TjvOrderReadDTO> orders = tables
            .stream()
            .map(table -> {
                // Order exists - add to this order, doesn't exist - create new
                List<TjvOrderReadDTO> onTable = orderService.findByTableID(table.getId());
                return onTable.isEmpty() ? orderService.create(new TjvOrderCreateDTO(table.getId(),
                        Timestamp.from(Instant.now()))) : onTable.get(0);
            })
            .collect(Collectors.toList());

        // Add requested food in each order
        orders.forEach(order -> this.create(
            food.stream()
            .map ( dto -> new TjvOrderFoodCreateDTO(order.getId(), dto.getFood(),
                dto.getDatetime() == null ? Timestamp.from(Instant.now()) : dto.getDatetime(),
                dto.getCount()) )
            .collect(Collectors.toList())
        ));
        return orders;
    }

    public List<TjvOrderFoodReadDTO> getFoodAtTable (int tableId) {
        TjvTable table = tableService.getEntityByID(tableId);
        return repository.findByTable(table)
            .stream()
            .map(this::toDTO)
            .sorted( (left, right) -> Objects.equals(left.getFood().getCooked(), right.getFood().getCooked())
                    ? (""+left.getFood().getName()).compareToIgnoreCase(""+right.getFood().getName())
                    : left.getFood().getCooked().compareTo(right.getFood().getCooked()) )
            .collect(Collectors.toList());
    }

    @Override
    protected TjvOrderFoodReadDTO toDTO (TjvOrderFood entity) {
        return new TjvOrderFoodReadDTO(entity.getId(), entity.getOrder().getId(),
                foodService.toDTO(entity.getFood()), entity.getDatetime(), entity.getCount());
    }

    @Override
    protected TjvOrderFood fromDTO (TjvOrderFoodCreateDTO dto) {
        return new TjvOrderFood(
            orderService.getEntityByID(dto.getOrder()),
            foodService.getEntityByID(dto.getFood()),
            dto.getDatetime(),
            dto.getCount()
        );
    }

    @Override
    protected TjvOrderFood merge (TjvOrderFood entity, TjvOrderFoodCreateDTO dto) {
        // Relationships
        if (dto.getOrder() != null)
            entity.setOrder(orderService.getEntityByID(dto.getOrder()));
        if (dto.getFood() != null)
            entity.setFood(foodService.getEntityByID(dto.getFood()));

        // Properties
        if (dto.getDatetime() != null)
            entity.setDatetime(dto.getDatetime());
        if (dto.getCount() != null)
            entity.setCount(dto.getCount());

        return entity;
    }

}
