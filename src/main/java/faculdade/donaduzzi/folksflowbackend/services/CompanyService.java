package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private static final String COMPANY_NOT_FOUND = "Company not found";

    private final CompanyRepository companyRepository;
    private final FileStorageService fileStorageService;

    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyResponse::fromEntity)
                .toList();
    }

    public CompanyResponse findById(Integer id) {
        return companyRepository.findById(id)
                .map(CompanyResponse::fromEntity)
                .orElseThrow(() -> new BusinessException(COMPANY_NOT_FOUND));
    }

    public Company findEntityById(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        Company company = new Company();
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setWebsite(request.getWebsite());
        if (request.getProfilePhoto() != null) {
            company.setProfilePhoto(request.getProfilePhoto());
        }
        company.setIsActive(true);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        
        Company savedCompany = companyRepository.save(company);
        return CompanyResponse.fromEntity(savedCompany);
    }

    @Transactional
    public CompanyResponse update(Integer id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMPANY_NOT_FOUND));
        
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setWebsite(request.getWebsite());
        if (request.getProfilePhoto() != null) {
            company.setProfilePhoto(request.getProfilePhoto());
        }
        company.setUpdatedAt(LocalDateTime.now());

        Company updatedCompany = companyRepository.save(company);
        return CompanyResponse.fromEntity(updatedCompany);
    }

    @Transactional
    public String uploadLogo(Integer id, MultipartFile file) {
        Company company = findEntityById(id);
        String fileName = fileStorageService.storeFile(file);
        company.setProfilePhoto(fileName);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
        return fileName;
    }

    @Transactional
    public void delete(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMPANY_NOT_FOUND));
        company.setIsActive(false);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }
}
