package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.PriorityResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/priorities")
@RequiredArgsConstructor
public class PriorityController {
    private final PriorityRepository repository;

    @GetMapping
    public ResponseEntity<List<PriorityResponse>> getAll() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(PriorityResponse::fromEntity)
                .toList());
    }

    @PostMapping
    public ResponseEntity<Priority> create(@RequestBody Priority priority) {
        return ResponseEntity.ok(repository.save(priority));
    }
}
