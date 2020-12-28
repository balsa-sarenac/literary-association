package upp.team5.literaryassociation.authorRegistration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.team5.literaryassociation.common.dto.UserDTO;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {
    private Long id;
    private UserDTO user;
//    private List<FileDTO> files;
}
