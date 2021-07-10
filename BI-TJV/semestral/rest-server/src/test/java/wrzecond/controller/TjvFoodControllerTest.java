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
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.service.TjvFoodService;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvFoodControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TjvFoodController controller;

    @MockBean
    private TjvFoodService service;

    @Test
    void getAllergensInFood () throws Exception {
        // Prepare data
        TjvAllergenReadDTO dto = new TjvAllergenReadDTO(1, "Lepek"),
                dto2 = new TjvAllergenReadDTO(2, "Mleko");

        // Mock
        BDDMockito.given(service.getAllergensInFood(1)).willReturn(Arrays.asList(dto, dto2));
        BDDMockito.given(service.getAllergensInFood(2)).willThrow(EntityNotFoundException.class);

        // Build request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/food/allergens/1");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(dto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(dto.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(dto2.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", CoreMatchers.is(dto2.getName())));
        BDDMockito.verify(service, Mockito.atLeastOnce()).getAllergensInFood(1);

        // Test error
        builder = MockMvcRequestBuilders.get("/food/allergens/2");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        BDDMockito.verify(service, Mockito.atLeastOnce()).getAllergensInFood(2);
    }

}
