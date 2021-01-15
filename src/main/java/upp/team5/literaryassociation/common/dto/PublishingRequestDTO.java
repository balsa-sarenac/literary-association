package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishingRequestDTO {
    private Long id;
    private BookDTO book;
    private Boolean approved;
    private List<NoteDTO> notes;
    private List<FileDTO> potentialPlagiarismList;
}
