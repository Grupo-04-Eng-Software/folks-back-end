package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/priorities")
@RequiredArgsConstructor
public class PriorityController {
    private final PriorityRepository repository;

    @PostMapping
    public ResponseEntity<Priority> create(@RequestBody Priority priority) {
        return ResponseEntity.ok(repository.save(priority));
    }
}
