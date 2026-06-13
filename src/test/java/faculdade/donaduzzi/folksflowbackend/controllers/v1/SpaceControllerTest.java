package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.dto.SpaceRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.SpaceResponse;
import faculdade.donaduzzi.folksflowbackend.services.SpaceService;
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
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpaceService spaceService;

    @Test
    @WithMockUser
    @DisplayName("Should list all spaces for logged user")
    void listSpacesSuccess() throws Exception {
        when(spaceService.findAllByUser(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/spaces"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 403 when user is not authenticated")
    void listSpacesUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/spaces"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 when creating a valid space")
    void createSpaceSuccess() throws Exception {
        SpaceRequest request = SpaceRequest.builder()
                .name("Meu Space")
                .description("Descrição do space")
                .build();

        SpaceResponse response = SpaceResponse.builder()
                .spaceId(1)
                .name("Meu Space")
                .description("Descrição do space")
                .isActive(true)
                .build();

        when(spaceService.create(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating a space with missing required fields")
    void createSpaceValidationFailure() throws Exception {
        SpaceRequest request = SpaceRequest.builder()
                .name("")          // @NotBlank - campo inválido
                .description("")   // @NotBlank - campo inválido
                .build();

        mockMvc.perform(post("/api/v1/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 200 when updating an existing space")
    void updateSpaceSuccess() throws Exception {
        SpaceRequest request = SpaceRequest.builder()
                .name("Space Atualizado")
                .description("Nova descrição")
                .build();

        SpaceResponse response = SpaceResponse.builder()
                .spaceId(1)
                .name("Space Atualizado")
                .description("Nova descrição")
                .isActive(true)
                .build();

        when(spaceService.update(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/spaces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 204 when admin deletes a space")
    void deleteSpaceAsAdminSuccess() throws Exception {
        doNothing().when(spaceService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/spaces/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when non-admin tries to delete a space")
    void deleteSpaceAsUserForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/spaces/1"))
                .andExpect(status().isForbidden());
    }
}
