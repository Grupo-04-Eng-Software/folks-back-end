package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.dto.DepartmentRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.DepartmentResponse;
import faculdade.donaduzzi.folksflowbackend.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentResponse>> getByCompany(@PathVariable Integer companyId) {
        return ResponseEntity.ok(departmentService.findByCompany(companyId));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@RequestBody @Valid DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Integer id, @RequestBody @Valid DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
