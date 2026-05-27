package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.security.TokenService;
import faculdade.donaduzzi.folksflowbackend.model.entities.RefreshToken;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.repository.RefreshTokenRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshToken.setIsRevoked(false);
        
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now()) || token.getIsRevoked()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired or revoked. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
