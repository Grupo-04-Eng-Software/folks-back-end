package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.ProjectRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.ProjectResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<ProjectResponse>> getProjectsBySpace(@PathVariable Integer spaceId) {
        return ResponseEntity.ok(projectService.findAllBySpace(spaceId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.create(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Integer id, @RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Empresas vinculadas ao projeto ─────────────────────────────────────────

    @GetMapping("/{id}/companies")
    public ResponseEntity<List<CompanyResponse>> getProjectCompanies(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.listCompanies(id));
    }

    @PostMapping("/{id}/companies/{companyId}")
    public ResponseEntity<Void> addCompany(@PathVariable Integer id, @PathVariable Integer companyId) {
        projectService.associateCompany(id, companyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/companies/{companyId}")
    public ResponseEntity<Void> removeCompany(@PathVariable Integer id, @PathVariable Integer companyId) {
        projectService.disassociateCompany(id, companyId);
        return ResponseEntity.noContent().build();
    }

    // ── Candidatos vinculados ao projeto ───────────────────────────────────────

    @GetMapping("/{id}/candidates")
    public ResponseEntity<List<CandidateResponse>> getProjectCandidates(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.listCandidates(id));
    }

    @PostMapping("/{id}/candidates/{candidateId}")
    public ResponseEntity<Void> addCandidate(@PathVariable Integer id, @PathVariable Integer candidateId) {
        projectService.associateCandidate(id, candidateId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/candidates/{candidateId}")
    public ResponseEntity<Void> removeCandidate(@PathVariable Integer id, @PathVariable Integer candidateId) {
        projectService.disassociateCandidate(id, candidateId);
        return ResponseEntity.noContent().build();
    }
}
