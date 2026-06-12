package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.DTO.ProjectRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.ProjectResponse;
import faculdade.donaduzzi.folksflowbackend.services.ProjectService;
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
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @Test
    @WithMockUser
    @DisplayName("Should return 200 and list of projects for a given space")
    void getProjectsBySpaceSuccess() throws Exception {
        when(projectService.findAllBySpace(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/projects/space/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 403 when listing projects without authentication")
    void getProjectsBySpaceUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/projects/space/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 when creating a valid project")
    void createProjectSuccess() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("Projeto Teste")
                .description("Descrição do projeto")
                .spaceId(1)
                .build();

        ProjectResponse response = ProjectResponse.builder()
                .projectId(1)
                .name("Projeto Teste")
                .description("Descrição do projeto")
                .isActive(true)
                .build();

        when(projectService.create(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating a project with missing required fields")
    void createProjectValidationFailure() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("") // @NotBlank - campo inválido
                .build(); // spaceId ausente (@NotNull)

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 when updating an existing project")
    void updateProjectSuccess() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("Projeto Atualizado")
                .description("Nova descrição")
                .spaceId(1)
                .build();

        ProjectResponse response = ProjectResponse.builder()
                .projectId(1)
                .name("Projeto Atualizado")
                .description("Nova descrição")
                .isActive(true)
                .build();

        when(projectService.update(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 204 when admin deletes a project")
    void deleteProjectAsAdminSuccess() throws Exception {
        doNothing().when(projectService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/projects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when non-admin tries to delete a project")
    void deleteProjectAsUserForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/projects/1"))
                .andExpect(status().isForbidden());
    }
}
