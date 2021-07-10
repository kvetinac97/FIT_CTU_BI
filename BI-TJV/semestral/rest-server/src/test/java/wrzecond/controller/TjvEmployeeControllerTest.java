package wrzecond.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wrzecond.employee.TjvEmployeeReadDTO;
import wrzecond.employee.TjvPasswordDTO;
import wrzecond.entity.TjvEmployee;
import wrzecond.service.TjvEmployeeService;

import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvEmployeeControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TjvEmployeeController controller;

    @MockBean
    private TjvEmployeeService authService;

    @Test
    void findAll () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getByID () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(String.format(controller.getCreateLocationPath(), 1));
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void passChange () throws Exception {
        // Entity, mock
        TjvEmployee user = new TjvEmployee("user01", "password", "Admin", "Admin", true);
        TjvPasswordDTO dto = new TjvPasswordDTO("password", "password2");
        TjvPasswordDTO dto2 = new TjvPasswordDTO("pass", "pass2");

        BDDMockito.doNothing().when(authService).updatePassword(user, dto.getOldPassword(), dto.getNewPassword());
        BDDMockito.doThrow(DataIntegrityViolationException.class).when(authService).updatePassword(user, dto2.getOldPassword(), dto2.getNewPassword());

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/employees/passChange")
            .contentType("application/json")
            .content("{\n" +
                    "    \"oldPassword\": \"password\",\n" +
                    "    \"newPassword\": \"password2\"\n" +
                    "}");

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        // Test
        mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(authService, Mockito.atLeastOnce()).updatePassword(user, dto.getOldPassword(), dto.getNewPassword());

        // Build request
        MockHttpServletRequestBuilder builder2 = MockMvcRequestBuilders.patch("/employees/passChange")
            .header("Username", user.getUsername())
            .header("Password", user.getPassword())
            .contentType("application/json")
            .content("{\n" +
                    "    \"oldPassword\": \"pass\",\n" +
                    "    \"newPassword\": \"pass2\"\n" +
                    "}");

        // Test
        mvc.perform(builder2)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(authService, Mockito.atLeastOnce()).updatePassword(user, dto2.getOldPassword(), dto2.getNewPassword());
    }

    @Test
    void login () throws Exception {
        // Entity
        TjvEmployee user = new TjvEmployee("testlog", "logmein", "Test", "Login", false);
        ReflectionTestUtils.setField(user, "id", (new Random()).nextInt());
        TjvEmployeeReadDTO dto = new TjvEmployeeReadDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getAdmin());

        // Mock
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        BDDMockito.given(authService.getByID(user.getId())).willReturn(dto);

        // Unauthorized
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/login");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Login successful
        builder = builder.header("Username", user.getUsername())
            .header("Password", user.getPassword());

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(dto.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(dto.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(dto.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.admin", CoreMatchers.is(dto.getAdmin())));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByID(user.getId());
    }

}
