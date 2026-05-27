package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.security.TokenService;
import faculdade.donaduzzi.folksflowbackend.model.DTO.LoginRequestDTO;
import faculdade.donaduzzi.folksflowbackend.model.DTO.LoginResponseDTO;
import faculdade.donaduzzi.folksflowbackend.model.DTO.RegisterRequestDTO;
import faculdade.donaduzzi.folksflowbackend.model.entities.RefreshToken;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.enums.UserRole;
import faculdade.donaduzzi.folksflowbackend.repository.RefreshTokenRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponseDTO login(LoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(body.Password(), user.getPasswordHash())) {
            String accessToken = tokenService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            return new LoginResponseDTO(user.getName(), accessToken, refreshToken.getToken());
        }
        throw new RuntimeException("Invalid password");
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO body) {
        if (userRepository.findByEmail(body.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User newUser = new User();
        newUser.setName(body.name());
        newUser.setEmail(body.email());
        newUser.setPasswordHash(passwordEncoder.encode(body.password()));
        newUser.setRole(UserRole.USER);
        newUser.setIsActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        // Nota: O Address precisaria ser tratado aqui ou vir no DTO. 
        // Para este MVP, vamos focar no fluxo de auth.

        userRepository.save(newUser);

        String accessToken = tokenService.generateToken(newUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(newUser);
        
        return new LoginResponseDTO(newUser.getName(), accessToken, refreshToken.getToken());
    }

    public LoginResponseDTO refreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = tokenService.generateToken(user);
                    return new LoginResponseDTO(user.getName(), accessToken, token);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }
}
