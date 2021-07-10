package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.allergen.TjvAllergenCreateDTO;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.entity.TjvAllergen;
import wrzecond.repository.TjvAllergenRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class tests TjvService.
 * As TjvService is generic template class,
 * I use TjvAllergenService for testing it.
 */
@SpringBootTest
public class TjvServiceTest {

    @Autowired
    private TjvAllergenService service;

    @MockBean
    private TjvAllergenRepository repository; // controlled by test

    @Test
    void getEntityByID () {
        TjvAllergen allergen = new TjvAllergen("Wheat");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));

        // Mock definition
        BDDMockito.given(repository.getOne(allergen.getId())).willReturn(allergen);
        BDDMockito.given(repository.getOne(1000)).willThrow(EntityNotFoundException.class);

        // Test working
        Assertions.assertEquals(allergen, service.getEntityByID(allergen.getId()));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(allergen.getId());

        // Test exception
        EntityNotFoundException exception = null;
        try { service.getEntityByID(1000); }
        catch (EntityNotFoundException x) { exception = x; }
        Assertions.assertNotNull(exception);
    }

    @Test
    void getByID () {
        TjvAllergen allergen = new TjvAllergen("Wheat");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), "Wheat");

        // Mock definition
        BDDMockito.given(repository.getOne(allergen.getId())).willReturn(allergen);
        BDDMockito.given(repository.getOne(1000)).willThrow(EntityNotFoundException.class);

        // Test working
        Assertions.assertEquals(dto, service.getByID(allergen.getId()));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(allergen.getId());

        // Test exception
        EntityNotFoundException exception = null;
        try { service.getByID(1000); }
        catch (EntityNotFoundException x) { exception = x; }
        Assertions.assertNotNull(exception);
    }

    @Test
    void findByIDs () {
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen("Milk");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));

        // Mock definition
        BDDMockito.given(repository.getOne(allergen.getId())).willReturn(allergen);
        BDDMockito.given(repository.getOne(allergen2.getId())).willReturn(allergen2);

        // Test working
        Assertions.assertEquals(service.findByIDs(Arrays.asList(allergen.getId(), allergen2.getId())), Arrays.asList(allergen, allergen2));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(allergen.getId());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(allergen2.getId());

        Assertions.assertEquals(service.findByIDs(Collections.singletonList(2000)), Collections.emptyList());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(2000);
    }

    @Test
    void findAll () {
        // Test data
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen("Milk");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), "Wheat");
        TjvAllergenReadDTO dto2 = new TjvAllergenReadDTO(allergen2.getId(), "Milk");

        // Mock definition
        BDDMockito.given(repository.findAll(service.getSort())).willReturn(
            new ArrayList<TjvAllergen>() {{
                add(allergen); add(allergen2);
            }}
        );

        // Test working
        Assertions.assertEquals(service.findAll(), Arrays.asList(dto, dto2));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).findAll(service.getSort());
    }

    @Test
    void create () {
        // Test data
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen(null);
        TjvAllergenCreateDTO c_dto = new TjvAllergenCreateDTO("Wheat"),
                c_dto2 = new TjvAllergenCreateDTO(null);
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), "Wheat");

        // Mock definition
        BDDMockito.given(repository.save(allergen)).willReturn(allergen);
        BDDMockito.given(repository.save(allergen2)).willThrow(DataIntegrityViolationException.class);

        // Test working
        Assertions.assertEquals(dto, service.create(c_dto));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(allergen);

        // Test exception
        DataIntegrityViolationException exception = null;
        try { service.create(c_dto2); }
        catch (DataIntegrityViolationException x) { exception = x; }

        Assertions.assertNotNull(exception);
    }

    @Test
    void createAll () {
        // Test data
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen("Milk"),
            allergen3 = new TjvAllergen(null);
        TjvAllergenCreateDTO c_dto = new TjvAllergenCreateDTO("Wheat"),
                c_dto2 = new TjvAllergenCreateDTO("Milk"),
                c_dto3 = new TjvAllergenCreateDTO(null);
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(allergen.getId(), "Wheat"),
                dto2 = new TjvAllergenReadDTO(allergen2.getId(), "Milk");

        // Mock definition
        BDDMockito.given(repository.save(allergen)).willReturn(allergen);
        BDDMockito.given(repository.save(allergen2)).willReturn(allergen2);
        BDDMockito.given(repository.save(allergen3)).willThrow(DataIntegrityViolationException.class);

        // Test working
        Assertions.assertEquals(Arrays.asList(dto, dto2), service.create(Arrays.asList(c_dto, c_dto2)));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(allergen);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(allergen2);

        // Test exception
        DataIntegrityViolationException exception = null;
        try { service.create(Arrays.asList(c_dto2, c_dto3)); }
        catch (DataIntegrityViolationException x) { exception = x; }

        Assertions.assertNotNull(exception);
    }

    @Test
    void update () {
        TjvAllergen allergen = new TjvAllergen("Wheat"), allergen2 = new TjvAllergen(null);
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(allergen2, "id", 1000 + (new Random()).nextInt(1000));
        TjvAllergenCreateDTO c_dto = new TjvAllergenCreateDTO("Wheat"),
                c_dto2 = new TjvAllergenCreateDTO(null),
                c_dto3 = new TjvAllergenCreateDTO("Stone");

        // Mock definition
        BDDMockito.given(repository.getOne(allergen.getId())).willReturn(allergen);
        BDDMockito.given(repository.getOne(allergen2.getId())).willReturn(allergen2);
        BDDMockito.given(repository.getOne(2000)).willThrow(EntityNotFoundException.class);
        BDDMockito.given(repository.save(allergen2)).willThrow(DataIntegrityViolationException.class);

        // Test working
        service.update(allergen.getId(), c_dto);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getOne(allergen.getId());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(allergen);

        // Test exception 1
        Exception exception = null;
        try { service.update(allergen2.getId(), c_dto2); }
        catch (DataIntegrityViolationException x) { exception = x; }
        Assertions.assertNotNull(exception);

        // Test exception 2
        exception = null;
        try { service.update(2000, c_dto3); }
        catch (EntityNotFoundException x) { exception = x; }
        Assertions.assertNotNull(exception);
    }

    @Test
    void delete () {
        // Mock definition
        BDDMockito.doNothing().when(repository).deleteById(1);
        BDDMockito.doThrow(EntityNotFoundException.class).when(repository).deleteById(2);

        // Test working
        service.delete(1);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).deleteById(1);

        // Test exception
        EntityNotFoundException exception = null;
        try { service.delete(2); }
        catch (EntityNotFoundException x) { exception = x; }
        Assertions.assertNotNull(exception);
    }

}
