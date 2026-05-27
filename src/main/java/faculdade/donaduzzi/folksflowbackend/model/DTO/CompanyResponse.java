package faculdade.donaduzzi.folksflowbackend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {
    private Integer companyId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private String profilePhoto;
    private Boolean isActive;

    public static CompanyResponse fromEntity(Company company) {
        return CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .name(company.getName())
                .email(company.getEmail())
                .phone(company.getPhone())
                .website(company.getWebsite())
                .profilePhoto(company.getProfilePhoto())
                .isActive(company.getIsActive())
                .build();
    }
}
