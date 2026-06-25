package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.UserRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.UserResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.enums.UserRole;
import faculdade.donaduzzi.folksflowbackend.repository.UserRepository;
import faculdade.donaduzzi.folksflowbackend.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse findById(Integer id) {
        return userRepository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BusinessException("Password is required for user creation");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setProfilePhoto(request.getProfilePhoto());
        user.setIsActive(request.getIsActive() == null || request.getIsActive());
        // Endereço é opcional: se não vier, usa o endereço padrão (id 1, do seeder).
        Integer addressId = request.getAddressId() != null ? request.getAddressId() : 1;
        user.setAddress(addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Address not found")));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Integer id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setProfilePhoto(request.getProfilePhoto());
        user.setUpdatedAt(LocalDateTime.now());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(UserRole.valueOf(request.getRole()));
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        if (request.getAddressId() != null) {
            user.setAddress(addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new BusinessException("Address not found")));
        }

        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public String uploadPhoto(Integer id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        String fileName = fileStorageService.storeFile(file);
        user.setProfilePhoto(fileName);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return fileName;
    }

    public User findEntityById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}
