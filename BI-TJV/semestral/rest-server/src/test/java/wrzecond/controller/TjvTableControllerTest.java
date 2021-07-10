package wrzecond.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TjvTableControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TjvTableController controller;

    @Test
    void all () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/tables/");
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getByID () throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(String.format(controller.getCreateLocationPath(), 1));
        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

}
