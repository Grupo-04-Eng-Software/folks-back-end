package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.DTO.UserRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.UserResponse;
import faculdade.donaduzzi.folksflowbackend.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 200 and list of users when admin is authenticated")
    void getAllUsersAsAdminSuccess() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when non-admin tries to list all users")
    void getAllUsersAsUserForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 403 when listing users without authentication")
    void getAllUsersUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 200 and user data when admin fetches user by id")
    void getUserByIdSuccess() throws Exception {
        UserResponse response = UserResponse.builder()
                .userId(1)
                .name("João Silva")
                .email("joao@email.com")
                .role("ADMIN")
                .isActive(true)
                .build();

        when(userService.findById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when non-admin tries to fetch user by id")
    void getUserByIdAsUserForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 200 when admin updates a user successfully")
    void updateUserSuccess() throws Exception {
        UserRequest request = UserRequest.builder()
                .addressId(1)
                .name("João Atualizado")
                .email("joao.atualizado@email.com")
                .password("senha123")
                .role("USER")
                .build();

        UserResponse response = UserResponse.builder()
                .userId(1)
                .name("João Atualizado")
                .email("joao.atualizado@email.com")
                .role("USER")
                .isActive(true)
                .build();

        when(userService.update(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@email.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 400 when updating a user with invalid data")
    void updateUserValidationFailure() throws Exception {
        UserRequest request = UserRequest.builder()
                .addressId(1)
                .name("")              // @NotBlank - campo inválido
                .email("email-invalido") // @Email - formato inválido
                .password("123")       // @Size(min = 6) - muito curto
                .role("USER")
                .build();

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
