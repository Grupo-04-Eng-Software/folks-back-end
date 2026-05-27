package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.SpaceRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.SpaceResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Space;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserSpace;
import faculdade.donaduzzi.folksflowbackend.repository.SpaceRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final UserSpaceRepository userSpaceRepository;

    public List<SpaceResponse> findAllByUser(User user) {
        return spaceRepository.findAllSpacesByUserId(user.getUserId())
                .stream()
                .map(SpaceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SpaceResponse create(SpaceRequest request, User user) {
        Space space = new Space();
        space.setName(request.getName());
        space.setDescription(request.getDescription());
        space.setProfilePhoto(request.getImageUrl());
        space.setIsActive(true);
        space.setCreatedAt(LocalDateTime.now());
        space.setUpdatedAt(LocalDateTime.now());

        Space savedSpace = spaceRepository.save(space);

        // Ao criar um espaço, o criador deve ser adicionado como membro
        UserSpace userSpace = new UserSpace();
        userSpace.setId(new UserSpace.UserSpaceId(user.getUserId(), savedSpace.getSpaceId()));
        userSpace.setSpace(savedSpace);
        userSpace.setUser(user);
        userSpace.setRole("OWNER");
        userSpaceRepository.save(userSpace);

        return SpaceResponse.fromEntity(savedSpace);
    }

    @Transactional
    public void delete(Integer id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space not found"));
        spaceRepository.delete(space);
    }

    public Space findById(Integer id) {
        return spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space not found"));
    }
}
