package faculdade.donaduzzi.folksflowbackend.model.dto;

import faculdade.donaduzzi.folksflowbackend.model.entities.Notification;
import faculdade.donaduzzi.folksflowbackend.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String link;

    public static NotificationResponse fromEntity(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .link(notification.getLink())
                .build();
    }
}
