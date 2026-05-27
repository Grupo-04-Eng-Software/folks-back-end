package faculdade.donaduzzi.folksflowbackend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponse {
    private Integer candidateId;
    private String name;
    private String email;
    private String phone;
    private String resume;
    private String linkedin;
    private String profilePhoto;
    private Boolean isActive;

    public static CandidateResponse fromEntity(Candidate candidate) {
        return CandidateResponse.builder()
                .candidateId(candidate.getCandidateId())
                .name(candidate.getName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .resume(candidate.getResume())
                .linkedin(candidate.getLinkedin())
                .profilePhoto(candidate.getProfilePhoto())
                .isActive(candidate.getIsActive())
                .build();
    }
}
