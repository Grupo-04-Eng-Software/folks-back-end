package faculdade.donaduzzi.folksflowbackend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Activity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponse {
    private Integer activityId;
    private String content;
    private String userName;
    private LocalDateTime createdAt;

    public static ActivityResponse fromEntity(Activity activity) {
        return ActivityResponse.builder()
                .activityId(activity.getActivityId())
                .content(activity.getContent())
                .userName(activity.getUser().getName())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
