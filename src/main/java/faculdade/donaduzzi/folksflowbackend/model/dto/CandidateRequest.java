package faculdade.donaduzzi.folksflowbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String email;
    
    private String phone;
    private String linkedin;
    private String profilePhoto;
    private Integer addressId;
}
