package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.food.TjvFoodCreateDTO;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.entity.TjvAllergen;
import wrzecond.entity.TjvFood;
import wrzecond.repository.TjvFoodRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TjvFoodService extends TjvService<TjvFood, TjvFoodReadDTO, TjvFoodCreateDTO> {

    // === PARAMETERS ===
    private final TjvAllergenService allergenService;
    private final TjvFoodRepository foodRepository;

    @Autowired
    public TjvFoodService (TjvFoodRepository foodRepository, TjvAllergenService service) {
        super (foodRepository);
        this.foodRepository = foodRepository;
        this.allergenService = service;
    }

    public List<TjvAllergenReadDTO> getAllergensInFood (int foodId) {
        return foodRepository.getAllergensInFood(getEntityByID(foodId))
            .stream().map(allergenService::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void delete (Integer id) {
        TjvFood food = getEntityByID(id);
        food.setAllergens(Collections.emptyList());
        foodRepository.saveAndFlush(food);
        super.delete(id);
    }

    @Override
    protected Sort getSort() {
        return Sort.by(Sort.Direction.ASC, "cooked", "name");
    }

    @Override
    protected TjvFoodReadDTO toDTO (TjvFood entity) {
        return new TjvFoodReadDTO (
            entity.getId(), entity.getName(), entity.getPrice(),
            entity.isCooked(),
            entity.getAllergens()
                .stream()
                .map(TjvAllergen::getId)
                .collect(Collectors.toList())
        );
    }

    @Override
    protected TjvFood fromDTO (TjvFoodCreateDTO dto) {
        return new TjvFood (
            dto.getName(),
            dto.getPrice(),
            dto.getCooked(),
            allergenService.findByIDs(dto.getAllergens())
        );
    }

    @Override
    protected TjvFood merge (TjvFood entity, TjvFoodCreateDTO dto) {
        if (dto.getName() != null)
            entity.setName(dto.getName());
        if (dto.getPrice() != null)
            entity.setPrice(dto.getPrice());
        if (dto.getCooked() != null)
            entity.setCooked(dto.getCooked());

        // Allergens
        entity.setAllergens(allergenService.findByIDs(dto.getAllergens()));

        return entity;
    }

}
