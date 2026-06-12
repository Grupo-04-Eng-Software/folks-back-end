package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.TaskRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.TaskResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> searchTasks(
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer priorityId,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer userId,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.searchTasks(statusId, priorityId, tagId, title, userId, pageable));
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Integer statusId) {
        return ResponseEntity.ok(taskService.findAllByStatus(statusId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.findOverdue());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.create(request, user));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<TaskResponse> moveTask(
            @PathVariable Integer id,
            @RequestParam Integer targetStatusId,
            @RequestParam Integer position) {
        return ResponseEntity.ok(taskService.moveTask(id, targetStatusId, position));
    }

    @PostMapping("/{id}/assign/{userId}")
    public ResponseEntity<Void> assignUser(@PathVariable Integer id, @PathVariable Integer userId) {
        taskService.assignUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/unassign/{userId}")
    public ResponseEntity<Void> unassignUser(@PathVariable Integer id, @PathVariable Integer userId) {
        taskService.unassignUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/checklist")
    public ResponseEntity<Void> addChecklistItem(@PathVariable Integer id, @RequestBody String content) {
        taskService.addChecklistItem(id, content);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/checklist/{itemId}/toggle")
    public ResponseEntity<Void> toggleChecklistItem(@PathVariable Integer itemId) {
        taskService.toggleChecklistItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/time/start")
    public ResponseEntity<Void> startTime(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        taskService.startTime(id, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/time/stop")
    public ResponseEntity<Void> stopTime(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        taskService.stopTime(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/time/total")
    public ResponseEntity<Long> getTotalTime(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTotalTimeSpent(id));
    }
}
