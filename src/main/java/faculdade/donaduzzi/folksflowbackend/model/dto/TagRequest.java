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
public class TagRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String color;
    
    @NotNull
    private Integer spaceId;
}
