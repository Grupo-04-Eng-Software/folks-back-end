package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CompanyResponse findById(Integer id) {
        return companyRepository.findById(id)
                .map(CompanyResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Transactional
    public void delete(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setIsActive(false);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }
}
