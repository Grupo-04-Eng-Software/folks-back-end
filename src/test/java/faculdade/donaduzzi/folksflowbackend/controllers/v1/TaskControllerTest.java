package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskResponse;
import faculdade.donaduzzi.folksflowbackend.services.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser
    @DisplayName("Should return 200 and a page of tasks when authenticated")
    void searchTasksSuccess() throws Exception {
        when(taskService.searchTasks(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 403 when searching tasks without authentication")
    void searchTasksUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 and list of tasks filtered by status")
    void getTasksByStatusSuccess() throws Exception {
        when(taskService.findAllByStatus(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tasks/status/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 and list of overdue tasks")
    void getOverdueTasksSuccess() throws Exception {
        when(taskService.findOverdue()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tasks/overdue"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 when creating a valid task")
    void createTaskSuccess() throws Exception {
        TaskRequest request = TaskRequest.builder()
                .title("Nova Tarefa")
                .statusId(1)
                .priorityId(1)
                .build();

        TaskResponse response = TaskResponse.builder()
                .taskId(1)
                .title("Nova Tarefa")
                .statusId(1)
                .priorityId(1)
                .build();

        when(taskService.create(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating a task with missing required fields")
    void createTaskValidationFailure() throws Exception {
        TaskRequest request = TaskRequest.builder()
                .title("")  // @NotBlank - campo inválido
                .build();   // statusId e priorityId ausentes (@NotNull)

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 204 when deleting an existing task")
    void deleteTaskSuccess() throws Exception {
        doNothing().when(taskService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 403 when deleting a task without authentication")
    void deleteTaskUnauthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 204 when assigning a user to a task")
    void assignUserSuccess() throws Exception {
        doNothing().when(taskService).assignUser(anyInt(), anyInt());

        mockMvc.perform(post("/api/v1/tasks/1/assign/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 204 when unassigning a user from a task")
    void unassignUserSuccess() throws Exception {
        doNothing().when(taskService).unassignUser(anyInt(), anyInt());

        mockMvc.perform(delete("/api/v1/tasks/1/unassign/2"))
                .andExpect(status().isNoContent());
    }
}
