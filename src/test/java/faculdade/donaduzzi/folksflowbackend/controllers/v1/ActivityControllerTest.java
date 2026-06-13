package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityResponse;
import faculdade.donaduzzi.folksflowbackend.services.ActivityService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivityService activityService;

    @Test
    @WithMockUser
    @DisplayName("Should list activities by task successfully")
    void getActivitiesByTaskSuccess() throws Exception {
        when(activityService.findAllByTask(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/activities/task/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 403 when listing activities without authentication")
    void getActivitiesByTaskUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/activities/task/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create activity successfully")
    void createActivitySuccess() throws Exception {
        ActivityRequest request = ActivityRequest.builder()
                .content("Minha atividade")
                .taskId(1)
                .build();

        ActivityResponse response = ActivityResponse.builder()
                .activityId(1)
                .content("Minha atividade")
                .build();

        when(activityService.create(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating activity with invalid data")
    void createActivityValidationFailure() throws Exception {
        ActivityRequest request = ActivityRequest.builder()
                .content("")
                .taskId(null)
                .build();

        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
