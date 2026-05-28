package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.SpaceRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.SpaceResponse;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
