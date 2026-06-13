package faculdade.donaduzzi.folksflowbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriorityResponse {
    private Integer priorityId;
    private String name;
    private Integer position;

    public static PriorityResponse fromEntity(Priority priority) {
        return PriorityResponse.builder()
                .priorityId(priority.getPriorityId())
                .name(priority.getName())
                .position(priority.getPosition())
                .build();
    }
}
