package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.allergen.TjvAllergenCreateDTO;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.entity.TjvAllergen;

import java.util.Random;

@SpringBootTest
public class TjvAllergenServiceTest {

    @Autowired
    private TjvAllergenService service;

    @Test
    void toDTO () {
        TjvAllergen allergen = new TjvAllergen("Wheat");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt());
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), "Wheat");
        Assertions.assertEquals(dto, service.toDTO(allergen));
    }

    @Test
    void fromDTO () {
        TjvAllergenCreateDTO dto = new TjvAllergenCreateDTO("Wheat");
        TjvAllergen allergen = new TjvAllergen("Wheat");
        Assertions.assertEquals(allergen, service.fromDTO(dto));
    }

    @Test
    void merge () {
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen("Milk");
        TjvAllergenCreateDTO dto = new TjvAllergenCreateDTO(null),
                dto2 = new TjvAllergenCreateDTO("Milk");

        // Test
        Assertions.assertEquals(allergen, service.merge(allergen, dto));
        Assertions.assertEquals(allergen2, service.merge(allergen, dto2));
    }

}
