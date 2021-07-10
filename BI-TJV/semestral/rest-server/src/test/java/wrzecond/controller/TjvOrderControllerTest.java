package wrzecond.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wrzecond.entity.TjvEmployee;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvOrderService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvOrderControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TjvOrderController controller;

    @MockBean
    private TjvOrderService service;
    @MockBean
    private TjvEmployeeService authService;

    @Test
    void getByID () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/orders/1");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void update () throws Exception {
        TjvOrderCreateDTO dto = new TjvOrderCreateDTO(1, new Timestamp(1605312000120L));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/orders/1")
            .contentType("application/json")
            .content("{\n" +
                    "    \"table\": \"1\",\n" +
                    "    \"datetime\": \"2020-11-14T00:00:00.120Z\"\n" +
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
    void all () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/orders/");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void create () throws Exception {
        TjvOrderCreateDTO dto = new TjvOrderCreateDTO(1, new Timestamp(1605312000000L));
        TjvOrderReadDTO rDto = new TjvOrderReadDTO(1, dto.getTable(), dto.getDatetime(), false);

        BDDMockito.given(service.create(dto)).willReturn(rDto);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/orders/")
            .contentType("application/json")
            .content("{\n" +
                    "    \"table\": 1,\n" +
                    "    \"datetime\": \"2020-11-14T00:00:00.000Z\"\n" +
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
    void findByTableID () throws Exception {
        // Entities, mock
        TjvOrderReadDTO dto = new TjvOrderReadDTO(1, (new Random()).nextInt(), Timestamp.from(Instant.now()), true);
        TjvOrderReadDTO dto2 = new TjvOrderReadDTO(2, dto.getTable(), new Timestamp(1606422072000L), false);
        BDDMockito.given(service.findByTableID(dto.getTable())).willReturn(Arrays.asList(dto, dto2));

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/orders/at/" + dto.getTable());
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        TjvEmployee user = new TjvEmployee("user", "pass", "Test", "User", false);
        BDDMockito.given(authService.getByUsernamePassword(user.getUsername(), user.getPassword())).willReturn(user);
        builder = builder.header("Username", user.getUsername())
                .header("Password", user.getPassword());

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].table", CoreMatchers.is(dto.getTable())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].completed", CoreMatchers.is(dto.getCompleted())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(dto2.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].datetime", CoreMatchers.is("2020-11-26T20:21:12.000+00:00")));

        BDDMockito.verify(authService, Mockito.atLeastOnce()).getByUsernamePassword(user.getUsername(), user.getPassword());
        BDDMockito.verify(service, Mockito.atLeastOnce()).findByTableID(dto.getTable());
    }

}
