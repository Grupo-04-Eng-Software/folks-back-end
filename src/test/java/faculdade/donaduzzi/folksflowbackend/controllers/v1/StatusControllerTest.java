package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.dto.StatusRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.StatusResponse;
import faculdade.donaduzzi.folksflowbackend.services.StatusService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatusService statusService;

    @Test
    @WithMockUser
    @DisplayName("Should list status by project successfully")
    void getStatusByProjectSuccess() throws Exception {
        when(statusService.findAllByProject(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/status/project/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create status successfully")
    void createStatusSuccess() throws Exception {
        StatusRequest request = StatusRequest.builder()
                .name("A Fazer")
                .color("#FF0000")
                .projectId(1)
                .build();

        StatusResponse response = StatusResponse.builder()
                .statusId(1)
                .name("A Fazer")
                .color("#FF0000")
                .build();

        when(statusService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating status with invalid data")
    void createStatusValidationFailure() throws Exception {
        StatusRequest request = StatusRequest.builder()
                .name("")
                .color("")
                .projectId(null)
                .build();

        mockMvc.perform(post("/api/v1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete status successfully")
    void deleteStatusSuccess() throws Exception {
        doNothing().when(statusService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/status/1"))
                .andExpect(status().isNoContent());
    }
}
