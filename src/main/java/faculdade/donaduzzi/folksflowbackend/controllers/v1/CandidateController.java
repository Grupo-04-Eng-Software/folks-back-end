package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.services.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

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
}
