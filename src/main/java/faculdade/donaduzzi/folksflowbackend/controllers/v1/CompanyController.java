package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.DTO.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CompanyService;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
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
}
