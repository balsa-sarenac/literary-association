package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {
    private Long id;
    private UserDTO user;
    private Boolean active;
    private List<FileDTO> files;
    private List<NoteDTO> notes;
}
