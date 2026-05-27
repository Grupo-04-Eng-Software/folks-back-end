package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.ActivityRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.ActivityResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.services.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByTask(@PathVariable Integer taskId) {
        return ResponseEntity.ok(activityService.findAllByTask(taskId));
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody @Valid ActivityRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.create(request, user));
    }
}
