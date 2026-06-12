package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.infra.security.TokenService;
import faculdade.donaduzzi.folksflowbackend.model.dto.LoginRequestDTO;
import faculdade.donaduzzi.folksflowbackend.model.dto.LoginResponseDTO;
import faculdade.donaduzzi.folksflowbackend.model.dto.RegisterRequestDTO;
import faculdade.donaduzzi.folksflowbackend.model.dto.UserResponse;
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

    private final faculdade.donaduzzi.folksflowbackend.repository.AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public UserResponse getMe(User user) {
        User fullUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));
        return UserResponse.fromEntity(fullUser);
    }

    public LoginResponseDTO login(LoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (passwordEncoder.matches(body.Password(), user.getPasswordHash())) {
            String accessToken = tokenService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            return new LoginResponseDTO(user.getName(), accessToken, refreshToken.getToken());
        }
        throw new BusinessException("Invalid password");
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO body) {
        if (userRepository.findByEmail(body.email()).isPresent()) {
            throw new BusinessException("Email already in use");
        }

        User newUser = new User();
        newUser.setName(body.name());
        newUser.setEmail(body.email());
        newUser.setPasswordHash(passwordEncoder.encode(body.password()));
        newUser.setRole(UserRole.USER);
        newUser.setIsActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        if (body.addressId() != null) {
            newUser.setAddress(addressRepository.findById(body.addressId())
                    .orElseThrow(() -> new BusinessException("Address not found")));
        } else {
            throw new BusinessException("Address ID is required for registration");
        }

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
                .orElseThrow(() -> new BusinessException("Refresh token not found"));
    }
}
