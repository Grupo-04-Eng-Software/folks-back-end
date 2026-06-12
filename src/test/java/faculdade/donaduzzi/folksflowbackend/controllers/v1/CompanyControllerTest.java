package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import faculdade.donaduzzi.folksflowbackend.model.DTO.CompanyRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import faculdade.donaduzzi.folksflowbackend.services.CompanyService;
import faculdade.donaduzzi.folksflowbackend.services.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private CandidateService candidateService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @WithMockUser
    @DisplayName("Should list all companies successfully")
    void getAllCompaniesSuccess() throws Exception {
        when(companyService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/companies"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should get company by id successfully")
    void getCompanyByIdSuccess() throws Exception {
        CompanyResponse response = CompanyResponse.builder()
                .companyId(1)
                .name("Empresa A")
                .build();
        when(companyService.findById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/api/v1/companies/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create company successfully")
    void createCompanySuccess() throws Exception {
        CompanyRequest request = CompanyRequest.builder()
                .name("Nova Empresa")
                .build();

        CompanyResponse response = CompanyResponse.builder()
                .companyId(1)
                .name("Nova Empresa")
                .build();

        when(companyService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when creating company with missing name")
    void createCompanyValidationFailure() throws Exception {
        CompanyRequest request = CompanyRequest.builder()
                .name("")
                .build();

        mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update company successfully")
    void updateCompanySuccess() throws Exception {
        CompanyRequest request = CompanyRequest.builder()
                .name("Empresa Atualizada")
                .build();

        CompanyResponse response = CompanyResponse.builder()
                .companyId(1)
                .name("Empresa Atualizada")
                .build();

        when(companyService.update(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should upload company logo successfully")
    void uploadLogoSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", MediaType.IMAGE_PNG_VALUE, "logo content".getBytes());
        when(companyService.uploadLogo(anyInt(), any())).thenReturn("logo.png");

        mockMvc.perform(multipart("/api/v1/companies/1/logo")
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete company successfully")
    void deleteCompanySuccess() throws Exception {
        doNothing().when(companyService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/companies/1"))
                .andExpect(status().isNoContent());
    }
}
