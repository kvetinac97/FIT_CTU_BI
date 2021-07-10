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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wrzecond.entity.*;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.order_food.TjvOrderFoodCreateDTO;
import wrzecond.order_food.TjvOrderFoodReadDTO;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvOrderFoodService;
import wrzecond.table.TjvTableType;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvOrderFoodControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TjvOrderFoodController controller;

    @MockBean
    private TjvOrderFoodService service;
    @MockBean
    private TjvEmployeeService authService;

    @Test
    void all () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/order-food/");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void getByID () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/order-food/1");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void update () throws Exception {
        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(1, 1, new Timestamp(1605312000000L), 3);
        BDDMockito.doNothing().when(service).update(1, dto);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/order-food/1")
            .contentType("application/json")
            .content("{\n" +
                    "    \"order\": 1,\n" +
                    "    \"food\": 1,\n" +
                    "    \"datetime\": \"2020-11-14T00:00:00.000Z\",\n" +
                    "    \"count\": 3\n" +
                    "}");

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).update(1, dto);
    }

    @Test
    void create () throws Exception {
        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(6, 2, new Timestamp(1605312000001L), 1);
        TjvFoodReadDTO foodDto = new TjvFoodReadDTO(2, "Dummy", 50, false, Collections.emptyList());
        TjvOrderFoodReadDTO rDto = new TjvOrderFoodReadDTO(42, dto.getOrder(), foodDto, dto.getDatetime(), dto.getCount());
        BDDMockito.given(service.create(dto)).willReturn(rDto);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order-food/")
            .contentType("application/json")
            .content("{\n" +
                    "    \"order\": 6,\n" +
                    "    \"food\": 2,\n" +
                    "    \"datetime\": \"2020-11-14T00:00:00.001Z\",\n" +
                    "    \"count\": 1\n" +
                    "}");

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"))
            .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.is(String.format(controller.getCreateLocationPath(), rDto.getId()))));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).create(dto);
    }

    @Test
    void delete () throws Exception {
        BDDMockito.doNothing().when(service).delete(55223);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/order-food/55223");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        // Test
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).delete(55223);
    }

    @Test
    void createAtTableType () throws Exception {
        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(null, 93, new Timestamp(1605614400000L), 1);
        TjvOrderFoodCreateDTO dto2 = new TjvOrderFoodCreateDTO(null, null, new Timestamp(1605614400000L), 1);
        TjvOrderFoodCreateDTO dto3 = new TjvOrderFoodCreateDTO(null, 111, new Timestamp(1605614400000L), 1);
        TjvOrderReadDTO rDto = new TjvOrderReadDTO(3, 12, new Timestamp(1131711132000L), true);

        BDDMockito.given(service.createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto)))
            .willReturn(Collections.singletonList(rDto));
        BDDMockito.given(service.createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto2)))
                .willThrow(EntityNotFoundException.class);
        BDDMockito.given(service.createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto3)))
                .willThrow(DataIntegrityViolationException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order-food/type/INSIDE")
            .contentType("application/json")
            .content("[{\"food\": 93, \"datetime\": \"2020-11-17T12:00:00Z\", \"count\": 1}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        builder.content("[{\"datetime\": \"2020-11-17T12:00:00Z\", \"count\": 1}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto2));

        builder.content("[{\"food\": 111, \"datetime\": \"2020-11-17T12:00:00Z\", \"count\": 1}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto3));

        // Test
        builder.content("[{\"food\": 93, \"datetime\": \"2020-11-17T12:00:00Z\", \"count\": 1}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(rDto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].table", CoreMatchers.is(rDto.getTable())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].datetime", CoreMatchers.is("2005-11-11T12:12:12.000+00:00")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].completed", CoreMatchers.is(rDto.getCompleted())));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrderAtTypeWithFood(TjvTableType.INSIDE, Collections.singletonList(dto));
    }

    @Test
    void createOrUpdateAtTable () throws Exception {
        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(null, 4632, new Timestamp(923010590000L), 8);
        TjvOrderFoodCreateDTO dto2 = new TjvOrderFoodCreateDTO(null, null, new Timestamp(923010590000L), 8);
        TjvOrderFoodCreateDTO dto3 = new TjvOrderFoodCreateDTO(null, 1000, new Timestamp(923010590000L), 8);
        TjvOrderReadDTO rDto = new TjvOrderReadDTO(10, 5, new Timestamp(1125308800000L), false);

        BDDMockito.given(service.createOrUpdateAtTable(5, Collections.singletonList(dto)))
            .willReturn(rDto);
        BDDMockito.given(service.createOrUpdateAtTable(5, Collections.singletonList(dto2)))
            .willThrow(EntityNotFoundException.class);
        BDDMockito.given(service.createOrUpdateAtTable(5, Collections.singletonList(dto3)))
            .willThrow(DataIntegrityViolationException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order-food/at/5")
            .contentType("application/json")
            .content("[{\"food\": 4632, \"datetime\": \"1999-04-01T23:49:50Z\", \"count\": 8}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        builder.content("[{\"datetime\": \"1999-04-01T23:49:50Z\", \"count\": 8}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrUpdateAtTable(5, Collections.singletonList(dto2));

        builder.content("[{\"food\": 1000, \"datetime\": \"1999-04-01T23:49:50Z\", \"count\": 8}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrUpdateAtTable(5, Collections.singletonList(dto3));

        // Test
        builder.content("[{\"food\": 4632, \"datetime\": \"1999-04-01T23:49:50Z\", \"count\": 8}]");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"))
            .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.is(String.format("/order/%d", rDto.getId()))));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).createOrUpdateAtTable(5, Collections.singletonList(dto));
    }

    @Test
    void foodInOrder () throws Exception {
        TjvFoodReadDTO foodDto1 = new TjvFoodReadDTO(234, "Dummy1", 12, false, Collections.emptyList()),
                foodDto2 = new TjvFoodReadDTO(13, "Dummy2", 50, true, Collections.emptyList());
        TjvOrderFoodReadDTO dto = new TjvOrderFoodReadDTO(1, 24, foodDto1, new Timestamp(45655347345000L), 4);
        TjvOrderFoodReadDTO dto2 = new TjvOrderFoodReadDTO(2, 24, foodDto2, new Timestamp(935652343000L), 2);

        BDDMockito.given(service.getFoodAtTable(24)).willReturn(Arrays.asList(dto, dto2));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/order-food/at/24");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].food.id", CoreMatchers.is(dto.getFood().getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].food.price", CoreMatchers.is(dto.getFood().getPrice())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].count", CoreMatchers.is(dto.getCount())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(dto2.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].food.name", CoreMatchers.is(dto2.getFood().getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].food.cooked", CoreMatchers.is(dto2.getFood().getCooked())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].order", CoreMatchers.is(dto2.getOrder())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].datetime", CoreMatchers.is("1999-08-26T07:25:43.000+00:00")));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).getFoodAtTable(24);

        builder = MockMvcRequestBuilders.get("/order-food/at/155")
            .header("Username", user.getUsername())
            .header("Password", user.getPassword());

        BDDMockito.given(service.getFoodAtTable(155)).willThrow(EntityNotFoundException.class);

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).getFoodAtTable(155);
    }

}
