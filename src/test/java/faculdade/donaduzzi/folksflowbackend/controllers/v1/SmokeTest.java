package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLoadContext() {
    }

    @Test
    void shouldReturn403WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/spaces"))
                .andExpect(status().isForbidden());
    }
}
