package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrzecond.allergen.TjvAllergenCreateDTO;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.entity.TjvAllergen;
import wrzecond.repository.TjvAllergenRepository;

@Service
public class TjvAllergenService extends TjvService<TjvAllergen, TjvAllergenReadDTO, TjvAllergenCreateDTO> {

    @Autowired
    public TjvAllergenService (TjvAllergenRepository allergenRepository) {
        super (allergenRepository);
    }

    @Override
    protected TjvAllergenReadDTO toDTO (TjvAllergen entity) {
        return new TjvAllergenReadDTO(entity.getId(), entity.getName());
    }

    @Override
    protected TjvAllergen fromDTO (TjvAllergenCreateDTO dto) {
        return new TjvAllergen(dto.getName());
    }

    @Override
    protected TjvAllergen merge (TjvAllergen entity, TjvAllergenCreateDTO dto) {
        if (dto.getName() != null)
            entity.setName(dto.getName());
        return entity;
    }

}
