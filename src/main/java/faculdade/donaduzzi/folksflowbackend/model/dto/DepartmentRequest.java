package faculdade.donaduzzi.folksflowbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {
    @NotBlank
    private String name;

    private String description;

    private String color;

    @NotNull
    private Integer companyId;
}
