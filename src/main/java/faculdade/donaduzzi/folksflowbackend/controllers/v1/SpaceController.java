package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.SpaceRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.SpaceResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.services.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @GetMapping
    public ResponseEntity<List<SpaceResponse>> getAllSpaces(@AuthenticationPrincipal User user) {
        System.out.println("TESTE DEBUG" + spaceService.findAllActive());
        return ResponseEntity.ok(spaceService.findAllActive());
    }

    @PostMapping
    public ResponseEntity<SpaceResponse> createSpace(@RequestBody @Valid SpaceRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spaceService.create(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceResponse> updateSpace(@PathVariable Integer id, @RequestBody @Valid SpaceRequest request) {
        return ResponseEntity.ok(spaceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpace(@PathVariable Integer id) {
        spaceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
