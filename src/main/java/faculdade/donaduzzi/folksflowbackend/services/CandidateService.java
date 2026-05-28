package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.repository.CandidateRepository;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.repository.specifications.CandidateSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final FileStorageService fileStorageService;

    public Page<CandidateResponse> findAll(Pageable pageable) {
        return candidateRepository.findAll(pageable)
                .map(CandidateResponse::fromEntity);
    }

    public Page<CandidateResponse> search(String name, String email, String linkedIn, Pageable pageable) {
        Specification<Candidate> spec = Specification.where(CandidateSpecifications.nameContains(name))
                .and(CandidateSpecifications.emailContains(email))
                .and(CandidateSpecifications.linkedInContains(linkedIn));

        return candidateRepository.findAll(spec, pageable).map(CandidateResponse::fromEntity);
    }

    public Candidate findById(Integer id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    public List<CandidateResponse> findByName(String name) {
        return candidateRepository.findByNameContainingIgnoreCase(name).stream()
                .map(CandidateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CandidateResponse> findByCompany(Integer companyId) {
        return candidateRepository.findByCompanyId(companyId).stream()
                .map(CandidateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void associateWithCompany(Integer candidateId, Integer companyId) {
        Candidate candidate = findById(candidateId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        candidate.getCompanies().add(company);
        candidateRepository.save(candidate);
    }

    @Transactional
    public String uploadResume(Integer candidateId, org.springframework.web.multipart.MultipartFile file) {
        Candidate candidate = findById(candidateId);
        String fileName = fileStorageService.storeFile(file);
        candidate.setResume(fileName);
        candidate.setUpdatedAt(java.time.LocalDateTime.now());
        candidateRepository.save(candidate);
        return fileName;
    }

    @Transactional
    public void delete(Integer id) {
        Candidate candidate = findById(id);
        candidate.setIsActive(false);
        candidate.setUpdatedAt(java.time.LocalDateTime.now());
        candidateRepository.save(candidate);
    }
}
