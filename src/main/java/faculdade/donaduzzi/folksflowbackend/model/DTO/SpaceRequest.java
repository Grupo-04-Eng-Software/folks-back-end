package faculdade.donaduzzi.folksflowbackend.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @Builder.Default
    private Boolean isActive = true;


}
