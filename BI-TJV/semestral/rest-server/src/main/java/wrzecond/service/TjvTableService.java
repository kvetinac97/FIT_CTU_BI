package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wrzecond.table.TjvTableCreateDTO;
import wrzecond.table.TjvTableReadDTO;
import wrzecond.entity.TjvTable;
import wrzecond.repository.TjvTableRepository;
import wrzecond.table.TjvTableType;

import java.util.List;

@Service
public class TjvTableService extends TjvService<TjvTable, TjvTableReadDTO, TjvTableCreateDTO> {

    // === PARAMETERS ===
    private final TjvTableRepository repository;

    @Autowired
    public TjvTableService (TjvTableRepository tableRepository) {
        super (tableRepository);
        this.repository = tableRepository;
    }

    public List<TjvTable> findByType (TjvTableType type) {
        return repository.findByType(type);
    }

    @Override
    protected Sort getSort () {
        return Sort.by(Sort.Direction.ASC, "id");
    }

    @Override
    protected TjvTableReadDTO toDTO (TjvTable entity) {
        return new TjvTableReadDTO(entity.getId(), entity.getType());
    }

    @Override
    protected TjvTable fromDTO (TjvTableCreateDTO dto) {
        return new TjvTable(dto.getType());
    }

    @Override
    protected TjvTable merge (TjvTable entity, TjvTableCreateDTO dto) {
        if (dto.getType() != null)
            entity.setType(dto.getType());

        return entity;
    }


}
