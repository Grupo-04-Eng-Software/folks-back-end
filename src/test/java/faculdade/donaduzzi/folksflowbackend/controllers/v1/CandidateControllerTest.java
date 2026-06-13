package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import faculdade.donaduzzi.folksflowbackend.services.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CandidateService candidateService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @WithMockUser
    @DisplayName("Should list all candidates successfully")
    void getAllCandidatesSuccess() throws Exception {
        Page<CandidateResponse> page = new PageImpl<>(Collections.emptyList());
        when(candidateService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/candidates"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create candidate successfully")
    void createCandidateSuccess() throws Exception {
        CandidateRequest request = CandidateRequest.builder()
                .name("Alice")
                .email("alice@email.com")
                .build();

        CandidateResponse response = CandidateResponse.builder()
                .candidateId(1)
                .name("Alice")
                .email("alice@email.com")
                .build();

        when(candidateService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating candidate with invalid data")
    void createCandidateValidationFailure() throws Exception {
        CandidateRequest request = CandidateRequest.builder()
                .name("")
                .email("")
                .build();

        mockMvc.perform(post("/api/v1/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update candidate successfully")
    void updateCandidateSuccess() throws Exception {
        CandidateRequest request = CandidateRequest.builder()
                .name("Alice Updated")
                .email("alice.updated@email.com")
                .build();

        CandidateResponse response = CandidateResponse.builder()
                .candidateId(1)
                .name("Alice Updated")
                .email("alice.updated@email.com")
                .build();

        when(candidateService.update(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should associate candidate with company successfully")
    void associateWithCompanySuccess() throws Exception {
        doNothing().when(candidateService).associateWithCompany(anyInt(), anyInt());

        mockMvc.perform(post("/api/v1/candidates/1/associate-company/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("Should upload resume successfully")
    void uploadResumeSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "resume.pdf", MediaType.APPLICATION_PDF_VALUE, "PDF content".getBytes());
        when(candidateService.uploadResume(anyInt(), any())).thenReturn("resume.pdf");

        mockMvc.perform(multipart("/api/v1/candidates/1/resume")
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete candidate successfully")
    void deleteCandidateSuccess() throws Exception {
        doNothing().when(candidateService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/candidates/1"))
                .andExpect(status().isNoContent());
    }
}
