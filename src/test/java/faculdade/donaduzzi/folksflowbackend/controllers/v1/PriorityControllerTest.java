package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriorityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PriorityRepository priorityRepository;

    @Test
    @WithMockUser
    @DisplayName("Should list all priorities successfully")
    void getAllPrioritiesSuccess() throws Exception {
        when(priorityRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/priorities"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create priority successfully")
    void createPrioritySuccess() throws Exception {
        Priority priority = new Priority();
        priority.setName("Alta");

        when(priorityRepository.save(any())).thenReturn(priority);

        mockMvc.perform(post("/api/v1/priorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priority)))
                .andExpect(status().isOk());
    }
}
