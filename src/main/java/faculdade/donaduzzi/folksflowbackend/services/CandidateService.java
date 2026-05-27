package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.repository.CandidateRepository;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;

    public List<CandidateResponse> findAll() {
        return candidateRepository.findAll().stream()
                .map(CandidateResponse::fromEntity)
                .collect(Collectors.toList());
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
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        candidate.getCompanies().add(company);
        candidateRepository.save(candidate);
    }
}
