package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.food.TjvFoodCreateDTO;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.entity.TjvAllergen;
import wrzecond.entity.TjvFood;
import wrzecond.repository.TjvFoodRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@SpringBootTest
public class TjvFoodServiceTest {

    @Autowired
    private TjvFoodService service;

    @MockBean
    private TjvFoodRepository repository;
    @MockBean
    private TjvAllergenService allergenService;

    @Test
    void getAllergensInFood () {
        // Prepare data
        TjvAllergen allergen = new TjvAllergen("Lepek"), allergen2 = new TjvAllergen("Milk");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));

        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), allergen.getName()),
            dto2 = new TjvAllergenReadDTO(allergen2.getId(), allergen2.getName());

        TjvFood food = new TjvFood("Kofola", 140, false, Arrays.asList(
            allergen, allergen2
        ));
        ReflectionTestUtils.setField(food, "id", (new Random()).nextInt());

        // Mock
        BDDMockito.given(allergenService.toDTO(allergen)).willReturn(dto);
        BDDMockito.given(allergenService.toDTO(allergen2)).willReturn(dto2);

        BDDMockito.given(repository.getOne(food.getId())).willReturn(food);
        BDDMockito.given(repository.getOne(-1)).willThrow(EntityNotFoundException.class);
        BDDMockito.given(repository.getAllergensInFood(food)).willReturn(Arrays.asList(allergen, allergen2));

        // Assert normal and exception
        Assertions.assertEquals(Arrays.asList(dto, dto2), service.getAllergensInFood(food.getId()));

        EntityNotFoundException exception = null;
        try {
            service.getAllergensInFood(-1);
        }
        catch (EntityNotFoundException x) {
            exception = x;
        }
        Assertions.assertNotNull(exception);
    }

    @Test
    void delete () {
        TjvAllergen allergen = new TjvAllergen("Lepek");
        TjvFood dummy = new TjvFood("Kofola", 50, false, Collections.singletonList(allergen));
        TjvFood dummy2 = new TjvFood(dummy.getName(), dummy.getPrice(), dummy.isCooked(), Collections.emptyList());

        // Mock definition
        BDDMockito.doNothing().when(repository).deleteById(1);
        BDDMockito.given(repository.getOne(1)).willReturn(dummy);
        BDDMockito.given(repository.saveAndFlush(dummy2)).willReturn(dummy2);

        // Test working
        service.delete(1);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).deleteById(1);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(1);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).saveAndFlush(dummy2);
    }

    @Test
    void toDTO () {
        TjvAllergen allergen = new TjvAllergen("Lepek"), allergen2 = new TjvAllergen("Milk");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));

        TjvFood food = new TjvFood("Kofola", 140, false, Arrays.asList(
            allergen, allergen2
        ));
        ReflectionTestUtils.setField(food, "id", (new Random()).nextInt());

        TjvFoodReadDTO dto = new TjvFoodReadDTO(food.getId(), "Kofola", 140, false, Arrays.asList(
            allergen.getId(), allergen2.getId()
        ));
        Assertions.assertEquals(dto, service.toDTO(food));
    }

    @Test
    void fromDTO () {
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen("Nuts");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));

        TjvFood food = new TjvFood("Hovezi polevka", 59, true, Arrays.asList(
            allergen, allergen2
        ));
        TjvFoodCreateDTO dto = new TjvFoodCreateDTO("Hovezi polevka", 59, true, Arrays.asList(
            allergen.getId(), allergen2.getId()
        ));

        // Mock definition
        BDDMockito.given(allergenService.findByIDs(Arrays.asList(allergen.getId(), allergen2.getId())))
            .willReturn(Arrays.asList(allergen, allergen2));

        Assertions.assertEquals(food, service.fromDTO(dto));
        BDDMockito.verify(allergenService, BDDMockito.atLeastOnce())
            .findByIDs(Arrays.asList(allergen.getId(), allergen2.getId()));
    }

    @Test
    void merge () {
        TjvAllergen allergen = new TjvAllergen("Feather"), allergen2 = new TjvAllergen("Meat");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));

        TjvFoodCreateDTO dto = new TjvFoodCreateDTO("Kure na paprice", 100, null, null),
            dto2 = new TjvFoodCreateDTO(null, null, false, Arrays.asList(
                allergen.getId(), allergen2.getId()
        ));
        TjvFood food = new TjvFood("Kure na zeli", 159, true, Collections.emptyList()),
                food2 = new TjvFood("Kure na paprice", 100, true, Collections.emptyList()),
                food3 = new TjvFood("Kure na paprice", 100, false, Arrays.asList(allergen, allergen2));

        // Mock definition
        BDDMockito.given(allergenService.findByIDs(Arrays.asList(allergen.getId(), allergen2.getId())))
            .willReturn(Arrays.asList(allergen, allergen2));

        // Test
        Assertions.assertEquals(food2, service.merge(food, dto));
        Assertions.assertEquals(food3, service.merge(food2, dto2));
        BDDMockito.verify(allergenService, BDDMockito.atLeastOnce())
            .findByIDs(Arrays.asList(allergen.getId(), allergen2.getId()));
    }

}
