package faculdade.donaduzzi.folksflowbackend.model.dto;

import faculdade.donaduzzi.folksflowbackend.model.entities.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Integer departmentId;
    private String name;
    private String description;
    private String color;
    private Integer companyId;

    public static DepartmentResponse fromEntity(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .description(department.getDescription())
                .color(department.getColor())
                .companyId(department.getCompany() != null ? department.getCompany().getCompanyId() : null)
                .build();
    }
}
