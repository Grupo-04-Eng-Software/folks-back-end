package faculdade.donaduzzi.folksflowbackend.model.dto;

public record RegisterRequestDTO(String name, String email, String password, Integer addressId) {
}
