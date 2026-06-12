package faculdade.donaduzzi.folksflowbackend.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String color;

    private Boolean isFinalStatus;
}
