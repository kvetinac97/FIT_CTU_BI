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
import wrzecond.employee.TjvEmployeeCreateDTO;
import wrzecond.employee.TjvEmployeeReadDTO;
import wrzecond.entity.TjvEmployee;
import wrzecond.repository.TjvEmployeeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Random;

@SpringBootTest
public class TjvEmployeeServiceTest {

    @Autowired
    private TjvEmployeeService service;

    @MockBean
    private TjvEmployeeRepository repository;

    @Test
    void getByUsernamePassword () {
        TjvEmployee employee = new TjvEmployee("john123", "password",
                "John", "Smith", true);
        ReflectionTestUtils.setField(employee, "id", (new Random()).nextInt());

        // Mock definition
        BDDMockito.given(repository.getByUsernamePassword(employee.getUsername(), employee.getPassword())).willReturn(employee);
        BDDMockito.given(repository.getByUsernamePassword("invalid", "pass")).willThrow(EntityNotFoundException.class);

        // Test
        Assertions.assertEquals(employee, service.getByUsernamePassword(employee.getUsername(), employee.getPassword()));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).getByUsernamePassword(employee.getUsername(), employee.getPassword());

        // Exception
        EntityNotFoundException exception = null;
        try { service.getByUsernamePassword("invalid", "pass"); }
        catch (EntityNotFoundException x) { exception = x; }

        Assertions.assertNotNull(exception);
    }

    @Test
    void updatePassword () {
        TjvEmployee employee = new TjvEmployee("john123", "password",
                "John", "Smith", true);
        ReflectionTestUtils.setField(employee, "id", (new Random()).nextInt());
        TjvEmployee employee2 = new TjvEmployee(employee.getUsername(), "pass",
                employee.getFirstName(), employee.getLastName(), employee.getAdmin());
        ReflectionTestUtils.setField(employee2, "id", employee.getId());

        // Mock
        BDDMockito.given(repository.save(employee2)).willReturn(employee2);

        // Test
        service.updatePassword(employee, employee.getPassword(), "pass");
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(employee2);

        // Exception
        DataIntegrityViolationException exception = null;
        try { service.updatePassword(employee, employee.getPassword().concat("bad"), "pass"); }
        catch (DataIntegrityViolationException x) { exception = x; }

        Assertions.assertNotNull(exception);
    }

    @Test
    void toDTO () {
        TjvEmployee employee = new TjvEmployee("john123", "password",
                "John", "Smith", true);
        ReflectionTestUtils.setField(employee, "id", (new Random()).nextInt());
        TjvEmployeeReadDTO dto = new TjvEmployeeReadDTO(employee.getId(), "john123",
                "John", "Smith", true);
        Assertions.assertEquals(dto, service.toDTO(employee));
    }

    @Test
    void fromDTO () {
        TjvEmployeeCreateDTO dto = new TjvEmployeeCreateDTO("john123", "password",
                "John", "Smith", true);
        TjvEmployee employee = new TjvEmployee("john123", "password",
                "John", "Smith", true);
        Assertions.assertEquals(employee, service.fromDTO(dto));
    }

    @Test
    void merge () {
        TjvEmployeeCreateDTO dto = new TjvEmployeeCreateDTO("wrzecond", null,
                null, "Jones", false);
        TjvEmployeeCreateDTO dto2 = new TjvEmployeeCreateDTO(null, "pass",
                "James", null, null);

        TjvEmployee employee = new TjvEmployee("john123", "password",
                "John", "Smith", true);
        TjvEmployee employee2 = new TjvEmployee("wrzecond", "password",
                "John", "Jones", false);
        TjvEmployee employee3 = new TjvEmployee("wrzecond", "pass",
                "James", "Jones", false);

        Assertions.assertEquals(employee2, service.merge(employee, dto));
        Assertions.assertEquals(employee3, service.merge(employee2, dto2));
    }

}
