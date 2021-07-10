package wrzecond.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import wrzecond.ICreateDTO;
import wrzecond.IReadDTO;
import wrzecond.entity.TjvEntity;
import wrzecond.repository.TjvRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class TjvService<T extends TjvEntity, R extends IReadDTO, C extends ICreateDTO> {

    // === PARAMETERS ===
    private final TjvRepository<T> repository;

    // === GET METHODS ===
    protected T getEntityByID (Integer id) {
        return repository.getOne(id);
    }
    public R getByID (Integer id) {
        return toDTO(getEntityByID(id));
    }
    public List<T> findByIDs (List<Integer> ids) {
        return ids.stream()
                .map(this::getEntityByID)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public List<R> findAll () {
        return repository.findAll(getSort())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // === POST METHODS ===
    public R create (C dto) {
        T entity = repository.save(fromDTO(dto));
        repository.flush();
        return toDTO(entity);
    }
    public List<R> create (List<C> dtoList) {
        List<T> entities = dtoList.stream()
            .map(this::fromDTO)
            .map(repository::save)
            .collect(Collectors.toList());
        repository.flush();
        return entities.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public void update (Integer id, C dto) {
        T entity = repository.getOne(id);
        repository.save(merge(entity, dto));
        repository.flush();
    }

    // === DELETE METHODS ===
    public void delete (Integer id) {
        repository.deleteById(id);
    }

    // === SORT ===
    protected Sort getSort () {
        return Sort.unsorted();
    }

    // === CONVERSION ===
    protected abstract R toDTO (T entity);
    protected abstract T fromDTO (C dto);
    protected abstract T merge (T entity, C dto);

}
