package faculdade.donaduzzi.folksflowbackend.model.dto;

import jakarta.validation.constraints.Email;
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
public class UserRequest {

    // Reference to an existing address (opcional). Ausente no update = mantém o
    // endereço atual; ausente na criação = usa o endereço padrão.
    private Integer addressId;

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Email
    private String email;


    // Opcional: obrigatório apenas na criação (validado no service).
    // No update, ausente/vazio significa "manter a senha atual".
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
