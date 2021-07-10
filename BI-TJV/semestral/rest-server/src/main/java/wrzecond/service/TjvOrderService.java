package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.TjvOrder;
import wrzecond.entity.TjvTable;
import wrzecond.repository.TjvOrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TjvOrderService extends TjvService<TjvOrder, TjvOrderReadDTO, TjvOrderCreateDTO> {

    // === PARAMETERS ===
    private final TjvOrderRepository orderRepository;
    private final TjvTableService tableService;

    @Autowired
    public TjvOrderService(TjvOrderRepository orderRepository, TjvTableService service) {
        super (orderRepository);
        this.orderRepository = orderRepository;
        this.tableService = service;
    }

    public List<TjvOrderReadDTO> findByTableID (int tableId) {
        // Table must exist
        TjvTable table = tableService.getEntityByID(tableId);
        if (table == null)
            throw new EntityNotFoundException();

        // Return (correct)
        return orderRepository.findByTableID(table)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    protected TjvOrderReadDTO toDTO (TjvOrder order) {
        return new TjvOrderReadDTO(order.getId(), order.getTable().getId(), order.getDatetime(), order.isCompleted());
    }

    @Override
    protected TjvOrder fromDTO (TjvOrderCreateDTO dto) {
        return new TjvOrder(
            tableService.getEntityByID(dto.getTable()),
            dto.getDatetime(), dto.getCompleted()
        );
    }

    @Override
    protected TjvOrder merge (TjvOrder entity, TjvOrderCreateDTO dto) {
        if (dto.getDatetime() != null)
            entity.setDatetime(dto.getDatetime());
        if (dto.getCompleted() != null)
            entity.setCompleted(dto.getCompleted());

        // Table
        if (dto.getTable() != null)
            entity.setTable(tableService.getEntityByID(dto.getTable()));

        return entity;
    }

}
