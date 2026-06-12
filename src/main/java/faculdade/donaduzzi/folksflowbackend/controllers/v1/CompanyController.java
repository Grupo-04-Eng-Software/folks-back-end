package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CompanyService;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import faculdade.donaduzzi.folksflowbackend.services.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CandidateService candidateService;
    private final FileStorageService fileStorageService;

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

    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String fileName = companyService.uploadLogo(id, file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/{id}/logo/download")
    public ResponseEntity<Resource> downloadLogo(@PathVariable Integer id) {
        var company = companyService.findById(id);
        if (company.getProfilePhoto() == null || company.getProfilePhoto().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = fileStorageService.loadFileAsPath(company.getProfilePhoto());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String fileName = companyService.uploadLogo(id, file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/{id}/logo/download")
    public ResponseEntity<Resource> downloadLogo(@PathVariable Integer id) {
        var company = companyService.findEntityById(id);
        if (company.getProfilePhoto() == null || company.getProfilePhoto().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = fileStorageService.loadFileAsPath(company.getProfilePhoto());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
