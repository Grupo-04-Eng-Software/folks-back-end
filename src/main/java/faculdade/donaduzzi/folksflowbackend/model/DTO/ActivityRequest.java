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
public class ActivityRequest {
    @NotBlank
    private String content;
    
    @NotNull
    private Integer taskId;
}
