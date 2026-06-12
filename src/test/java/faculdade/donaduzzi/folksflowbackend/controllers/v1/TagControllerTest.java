package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TagRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TagResponse;
import faculdade.donaduzzi.folksflowbackend.services.TagService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @Test
    @WithMockUser
    @DisplayName("Should list tags by space successfully")
    void getTagsBySpaceSuccess() throws Exception {
        when(tagService.findAllBySpace(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/tags/space/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create tag successfully")
    void createTagSuccess() throws Exception {
        TagRequest request = TagRequest.builder()
                .name("Bug")
                .color("#FF0000")
                .spaceId(1)
                .build();

        TagResponse response = TagResponse.builder()
                .tagId(1)
                .name("Bug")
                .color("#FF0000")
                .build();

        when(tagService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating tag with invalid data")
    void createTagValidationFailure() throws Exception {
        TagRequest request = TagRequest.builder()
                .name("")
                .color("")
                .spaceId(null)
                .build();

        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should associate tag with task successfully")
    void associateWithTaskSuccess() throws Exception {
        doNothing().when(tagService).associateWithTask(anyInt(), anyInt());

        mockMvc.perform(post("/api/v1/tags/task/1/associate/2"))
                .andExpect(status().isNoContent());
    }
}
