package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.StatusRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.StatusResponse;
import faculdade.donaduzzi.folksflowbackend.services.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<StatusResponse>> getStatusByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(statusService.findAllByProject(projectId));
    }

    @PostMapping
    public ResponseEntity<StatusResponse> createStatus(@RequestBody @Valid StatusRequest request) {
        return ResponseEntity.ok(statusService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        statusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
