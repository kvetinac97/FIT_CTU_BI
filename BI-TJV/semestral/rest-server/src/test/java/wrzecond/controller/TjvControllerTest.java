package wrzecond.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;
import wrzecond.entity.TjvEmployee;
import wrzecond.food.TjvFoodCreateDTO;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvFoodService;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TjvFoodController controller;

    @MockBean
    private TjvFoodService service;
    @MockBean
    private TjvEmployeeService authService;

    @Test
    void authenticate () {
        // Entity
        TjvEmployee user = new TjvEmployee("test", "123456", "Test", "User", false);
        TjvEmployee admin = new TjvEmployee("admin", "128!!!", "Admin", "Admin", true);

        // Mock config
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        BDDMockito.given(authService.getByUsernamePassword(admin.getUsername(), admin.getPassword())).willReturn(admin);

        // Assertions - not logged
        Assertions.assertNull(controller.authenticate(new MockHttpServletRequest(), TjvController.VisibilitySettings.ALL));

        for (TjvController.VisibilitySettings setting : new TjvController.VisibilitySettings[]{
                TjvController.VisibilitySettings.LOGGED, TjvController.VisibilitySettings.ADMIN}) {
            ResponseStatusException x = null;
            try { controller.authenticate(new MockHttpServletRequest(), setting); }
            catch (ResponseStatusException exc) { x = exc; }

            Assertions.assertNotNull(x);
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, x.getStatus());
        }

        // Assertions - logged as user
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Username", user.getUsername());
        request.addHeader("Password", user.getPassword());

        Assertions.assertEquals(user, controller.authenticate(request, TjvController.VisibilitySettings.ALL));
        Assertions.assertEquals(user, controller.authenticate(request, TjvController.VisibilitySettings.LOGGED));
        BDDMockito.verify(authService, Mockito.atLeast(2)).getByUsernamePassword(user.getUsername(), user.getPassword());

        ResponseStatusException x = null;
        try { controller.authenticate(request, TjvController.VisibilitySettings.ADMIN); }
        catch (ResponseStatusException exc) { x = exc; }

        Assertions.assertNotNull(x);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, x.getStatus());

        // Assertions - logged as admin
        MockHttpServletRequest adminRequest = new MockHttpServletRequest();
        adminRequest.addHeader("Username", admin.getUsername());
        adminRequest.addHeader("Password", admin.getPassword());

        Assertions.assertEquals(admin, controller.authenticate(adminRequest, TjvController.VisibilitySettings.ALL));
        Assertions.assertEquals(admin, controller.authenticate(adminRequest, TjvController.VisibilitySettings.LOGGED));
        Assertions.assertEquals(admin, controller.authenticate(adminRequest, TjvController.VisibilitySettings.ADMIN));

        for (MockHttpServletRequest req : Arrays.asList(new MockHttpServletRequest(), request, adminRequest)) {
            x = null;
            try { controller.authenticate(req, TjvController.VisibilitySettings.NONE); }
            catch (ResponseStatusException exc) { x = exc; }

            Assertions.assertNotNull(x);
            Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, x.getStatus());
        }
    }

    @Test
    void all () throws Exception {
        // Entities, mock
        TjvFoodReadDTO dto = new TjvFoodReadDTO(1, "Hovezi polevka", 60, true, Collections.emptyList());
        TjvFoodReadDTO dto2 = new TjvFoodReadDTO(2, "Cerealie s mlekem", 50, false, Arrays.asList(12, 25));
        BDDMockito.given(service.findAll()).willReturn(Arrays.asList(dto, dto2));

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/food/");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(dto.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].cooked", CoreMatchers.is(dto.getCooked())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(dto2.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", CoreMatchers.is(dto2.getPrice())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].allergens[0]", CoreMatchers.is(12)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].allergens[1]", CoreMatchers.is(25)));
        BDDMockito.verify(service, Mockito.atLeastOnce()).findAll();
    }

    @Test
    void create () throws Exception {
        TjvFoodCreateDTO dto = new TjvFoodCreateDTO("Kofola", 140, false, Collections.emptyList());
        TjvFoodCreateDTO dto2 = new TjvFoodCreateDTO("Kofola", 140, null, null);
        TjvFoodReadDTO rDto = new TjvFoodReadDTO(1, dto.getName(), dto.getPrice(), dto.getCooked(), dto.getAllergens());

        BDDMockito.given(service.create(dto)).willReturn(rDto);
        BDDMockito.given(service.create(dto2)).willThrow(DataIntegrityViolationException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/food/")
            .contentType("application/json")
            .content("{\n" +
                    "    \"name\": \"Kofola\",\n" +
                    "    \"price\": 140,\n" +
                    "    \"cooked\": false\n" +
                    "}");

        mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        TjvEmployee admin = new TjvEmployee("test", "admin", "Admin", "Admin", true);
        BDDMockito.given(authService.getByUsernamePassword(admin.getUsername(), admin.getPassword())).willReturn(admin);
        builder = MockMvcRequestBuilders.post("/food/")
            .header("Username", admin.getUsername())
            .header("Password", admin.getPassword())
            .contentType("application/json")
            .content("{\n" +
                    "    \"name\": \"Kofola\",\n" +
                    "    \"price\": 140,\n" +
                    "    \"cooked\": false\n" +
                    "}");

        mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.is(String.format(controller.getCreateLocationPath(), rDto.getId()))));
        BDDMockito.verify(service, Mockito.atLeastOnce()).create(dto);

        RequestBuilder builder2 = MockMvcRequestBuilders.post("/food/")
                .header("Username", admin.getUsername())
                .header("Password", admin.getPassword())
                .contentType("application/json")
                .content("{\n" +
                        "    \"name\": \"Kofola\",\n" +
                        "    \"price\": 140\n" +
                        "}");

        mvc.perform(builder2)
             .andExpect(MockMvcResultMatchers.status().isBadRequest());
        BDDMockito.verify(service, Mockito.atLeastOnce()).create(dto2);
    }

    @Test
    void update () throws Exception {
        // Entity, mock
        TjvFoodCreateDTO dto = new TjvFoodCreateDTO("Spagety s rajcatovou omackou", 99, true, null);
        TjvFoodCreateDTO dto2 = new TjvFoodCreateDTO(null, 250, null, Collections.singletonList(9999));
        BDDMockito.doNothing().when(service).update(1, dto);
        BDDMockito.doThrow(EntityNotFoundException.class).when(service).update(2, dto2);
        BDDMockito.doThrow(DataIntegrityViolationException.class).when(service).update(3, dto2);

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/food/1")
            .contentType("application/json")
            .content("{\n" +
                    "    \"name\": \"Spagety s rajcatovou omackou\",\n" +
                    "    \"price\": 99,\n" +
                    "    \"cooked\": true\n" +
                    "}");

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee admin = new TjvEmployee("test", "admin", "Admin", "Admin", true);
        BDDMockito.given(authService.getByUsernamePassword(admin.getUsername(), admin.getPassword())).willReturn(admin);
        builder = builder.header("Username", admin.getUsername())
                .header("Password", admin.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        BDDMockito.verify(service, Mockito.atLeastOnce()).update(1, dto);

        // Build request
        MockHttpServletRequestBuilder builder2 = MockMvcRequestBuilders.patch("/food/2")
            .header("Username", admin.getUsername())
            .header("Password", admin.getPassword())
            .contentType("application/json")
            .content("{\n" +
                    "    \"price\": 250,\n" +
                    "    \"allergens\": [9999]\n" +
                    "}");

        // Test
        mvc.perform(builder2)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).update(2, dto2);

        // Build request
        MockHttpServletRequestBuilder builder3 = MockMvcRequestBuilders.patch("/food/3")
            .header("Username", admin.getUsername())
            .header("Password", admin.getPassword())
            .contentType("application/json")
            .content("{\n" +
                    "    \"price\": 250,\n" +
                    "    \"allergens\": [9999]\n" +
                    "}");

        // Test
        mvc.perform(builder3)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        BDDMockito.verify(service, Mockito.atLeastOnce()).update(3, dto2);
    }

    @Test
    void getByID () throws Exception {
        // Entity, mock
        TjvFoodReadDTO dto = new TjvFoodReadDTO((new Random()).nextInt(), "Pizzasimo", 169, true, Arrays.asList(1, 7));
        BDDMockito.given(service.getByID(dto.getId())).willReturn(dto);
        BDDMockito.given(service.getByID(dto.getId() - 1)).willThrow(EntityNotFoundException.class);

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/food/" + dto.getId());
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cooked", CoreMatchers.is(dto.getCooked())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(dto.getPrice())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.allergens[0]", CoreMatchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.allergens[1]", CoreMatchers.is(7)));
        BDDMockito.verify(service, Mockito.atLeastOnce()).getByID(dto.getId());

        MockHttpServletRequestBuilder builder2 = MockMvcRequestBuilders.get("/food/" + (dto.getId() - 1));
        mvc.perform(builder2)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).getByID(dto.getId() - 1);
    }

    @Test
    void delete () throws Exception {
        // Entity, mock
        TjvEmployee admin = new TjvEmployee("test", "admin", "Admin", "Admin", true);
        BDDMockito.given(authService.getByUsernamePassword(admin.getUsername(), admin.getPassword())).willReturn(admin);
        BDDMockito.doNothing().when(service).delete(1);
        BDDMockito.doThrow(EmptyResultDataAccessException.class).when(service).delete(2);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/food/1")
            .header("Username", admin.getUsername())
            .header("Password", admin.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        BDDMockito.verify(service, Mockito.atLeastOnce()).delete(1);

        builder = MockMvcRequestBuilders.delete("/food/2")
            .header("Username", admin.getUsername())
            .header("Password", admin.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).delete(2);
    }

}
