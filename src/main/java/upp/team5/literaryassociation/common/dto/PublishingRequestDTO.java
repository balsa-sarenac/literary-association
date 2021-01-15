package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.publishing.dto.BookDTO;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishingRequestDTO {
    private Long id;
    private BookDTO bookDTO;
    private List<Note> notes;
    private List<FileDTO> potentialPlagiarismList;
}
