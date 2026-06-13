package faculdade.donaduzzi.folksflowbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusResponse {
    private Integer statusId;
    private String name;
    private String color;
    private Integer position;
    private Boolean isFinalStatus;

    public static StatusResponse fromEntity(Status status) {
        return StatusResponse.builder()
                .statusId(status.getStatusId())
                .name(status.getName())
                .color(status.getColor())
                .position(status.getPosition())
                .isFinalStatus(status.getIsFinalStatus())
                .build();
    }
}
