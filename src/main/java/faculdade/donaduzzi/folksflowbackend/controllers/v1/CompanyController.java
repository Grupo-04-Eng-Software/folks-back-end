package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CompanyService;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Integer id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @GetMapping("/{id}/candidates")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByCompany(@PathVariable Integer id) {
        return ResponseEntity.ok(candidateService.findByCompany(id));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody @Valid CompanyRequest request) {
        return ResponseEntity.ok(companyService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Integer id, @RequestBody @Valid CompanyRequest request) {
        return ResponseEntity.ok(companyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
