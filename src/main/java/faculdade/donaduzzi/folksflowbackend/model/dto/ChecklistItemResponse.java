package faculdade.donaduzzi.folksflowbackend.model.DTO;

import faculdade.donaduzzi.folksflowbackend.model.entities.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemResponse {
    private Integer id;
    private String content;
    private Boolean isCompleted;

    public static ChecklistItemResponse fromEntity(ChecklistItem item) {
        return new ChecklistItemResponse(item.getId(), item.getContent(), item.getIsCompleted());
    }
}
