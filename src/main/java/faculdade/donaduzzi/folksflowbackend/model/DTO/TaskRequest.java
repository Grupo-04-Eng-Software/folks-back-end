package faculdade.donaduzzi.folksflowbackend.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private Integer statusId;
    
    @NotNull
    private Integer priorityId;
    
    private LocalDate dueDate;
    
    private Integer position;
    
    private Integer parentTaskId;
}
