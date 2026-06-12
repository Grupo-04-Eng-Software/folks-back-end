package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Address;
import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.repository.AddressRepository;
import faculdade.donaduzzi.folksflowbackend.repository.CandidateRepository;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.repository.specifications.CandidateSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final AddressRepository addressRepository;
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
                .orElseThrow(() -> new BusinessException("Candidate not found"));
    }

    public List<CandidateResponse> findByName(String name) {
        return candidateRepository.findByNameContainingIgnoreCase(name).stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    public List<CandidateResponse> findByCompany(Integer companyId) {
        return candidateRepository.findByCompanyId(companyId).stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new BusinessException("Address not found"));
        } else {
            // Se não vier endereço, pegamos o primeiro do banco ou lançamos erro
            // Para simplificar, vamos lançar erro se não vier endereço e não houver um padrão
            throw new BusinessException("Address is required for candidate creation");
        }

        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setLinkedin(request.getLinkedin());
        candidate.setProfilePhoto(request.getProfilePhoto());
        candidate.setAddress(address);
        candidate.setResume(""); // Inicia vazio
        candidate.setIsActive(true);
        candidate.setCreatedAt(LocalDateTime.now());
        candidate.setUpdatedAt(LocalDateTime.now());

        Candidate savedCandidate = candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(savedCandidate);
    }

    @Transactional
    public CandidateResponse update(Integer id, CandidateRequest request) {
        Candidate candidate = findById(id);
        
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setLinkedin(request.getLinkedin());
        candidate.setProfilePhoto(request.getProfilePhoto());
        candidate.setUpdatedAt(LocalDateTime.now());

        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new BusinessException("Address not found"));
            candidate.setAddress(address);
        }

        Candidate updatedCandidate = candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(updatedCandidate);
    }

    @Transactional
    public void associateWithCompany(Integer candidateId, Integer companyId) {
        Candidate candidate = findById(candidateId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));

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
