package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import faculdade.donaduzzi.folksflowbackend.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getAllCandidates(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(candidateService.findByName(name));
        }
        return ResponseEntity.ok(candidateService.findAll());
    }

    @PostMapping("/{id}/associate-company/{companyId}")
    public ResponseEntity<Void> associateWithCompany(@PathVariable Integer id, @PathVariable Integer companyId) {
        candidateService.associateWithCompany(id, companyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String fileName = candidateService.uploadResume(id, file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/{id}/resume/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable Integer id) {
        var candidate = candidateService.findById(id);
        if (candidate.getResume() == null || candidate.getResume().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = fileStorageService.loadFileAsPath(candidate.getResume());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
