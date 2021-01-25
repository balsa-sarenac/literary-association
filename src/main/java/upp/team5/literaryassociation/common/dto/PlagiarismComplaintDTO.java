package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismComplaintDTO {
    private Long id;
    private BookDTO plagiarism;
    private BookDTO complainantBook;
    private List<NoteDTO> notes;
}
