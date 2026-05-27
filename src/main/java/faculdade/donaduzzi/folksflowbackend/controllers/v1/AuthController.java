package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.DTO.LoginRequestDTO;
import faculdade.donaduzzi.folksflowbackend.model.DTO.LoginResponseDTO;
import faculdade.donaduzzi.folksflowbackend.model.DTO.RegisterRequestDTO;
import faculdade.donaduzzi.folksflowbackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        return ResponseEntity.ok(authService.login(body));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        return ResponseEntity.ok(authService.register(body));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestParam String token) {
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}
