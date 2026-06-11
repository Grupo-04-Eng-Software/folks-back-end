package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.TagRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TagResponse;
import faculdade.donaduzzi.folksflowbackend.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<TagResponse>> getTagsBySpace(@PathVariable Integer spaceId) {
        return ResponseEntity.ok(tagService.findAllBySpace(spaceId));
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody @Valid TagRequest request) {
        return ResponseEntity.ok(tagService.create(request));
    }

    @PostMapping("/task/{taskId}/associate/{tagId}")
    public ResponseEntity<Void> associateWithTask(@PathVariable Integer taskId, @PathVariable Integer tagId) {
        tagService.associateWithTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/task/{taskId}/disassociate/{tagId}")
    public ResponseEntity<Void> disassociateFromTask(@PathVariable Integer taskId, @PathVariable Integer tagId) {
        tagService.disassociateFromTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}
