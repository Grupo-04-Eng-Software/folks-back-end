package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskResponse;
import faculdade.donaduzzi.folksflowbackend.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Integer statusId) {
        return ResponseEntity.ok(taskService.findAllByStatus(statusId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<TaskResponse> moveTask(
            @PathVariable Integer id,
            @RequestParam Integer targetStatusId,
            @RequestParam Integer position) {
        return ResponseEntity.ok(taskService.moveTask(id, targetStatusId, position));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
