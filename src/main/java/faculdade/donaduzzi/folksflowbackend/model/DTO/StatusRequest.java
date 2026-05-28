package faculdade.donaduzzi.folksflowbackend.model.DTO;

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
public class StatusRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String color;
    
    @NotNull
    private Integer projectId;
    
    private Integer position;

    @Builder.Default
    private Boolean isFinalStatus = false;
}
