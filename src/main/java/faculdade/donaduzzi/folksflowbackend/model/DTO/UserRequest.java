package faculdade.donaduzzi.folksflowbackend.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    // Reference to an existing address (address must already exist)
    @NotNull
    private Integer addressId;

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Email
    private String email;


    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @Size(max = 500)
    private String profilePhoto;

    @NotBlank
    @Size(max = 50)
    private String role;

    @Builder.Default
    private Boolean isActive = true;
}
