package faculdade.donaduzzi.folksflowbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Space;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceResponse {
    private Integer spaceId;
    private String name;
    private String description;
    private String profilePhoto;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SpaceResponse fromEntity(Space space) {
        return SpaceResponse.builder()
                .spaceId(space.getSpaceId())
                .name(space.getName())
                .description(space.getDescription())
                .profilePhoto(space.getProfilePhoto())
                .isActive(space.getIsActive())
                .createdAt(space.getCreatedAt())
                .updatedAt(space.getUpdatedAt())
                .build();
    }
}
