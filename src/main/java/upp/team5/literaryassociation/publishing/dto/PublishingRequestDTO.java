package upp.team5.literaryassociation.publishing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.Note;

import java.util.HashSet;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishingRequestDTO {
    Long id;
    Boolean approved;
    Book book;
    //HashSet<Note> notes;
}
